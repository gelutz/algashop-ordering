package com.lutz.algashop.ordering.domain.order;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.order.OrderItemPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.order.OrderItemPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomersPersistenceProvider;
import com.lutz.algashop.ordering.infrastructure.persistence.order.OrdersPersistenceProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import({
		OrdersPersistenceProvider.class,
		OrderPersistenceEntityAssembler.class,
		OrderItemPersistenceEntityAssembler.class,
		OrderPersistenceEntityDisassembler.class,
		OrderItemPersistenceEntityDisassembler.class,
		CustomersPersistenceProvider.class,
		CustomerPersistenceEntityAssembler.class,
		CustomerPersistenceEntityDisassembler.class
})
class OrdersIT {
	private final Orders sut;
	private final Customers customers;

	@Autowired
	public OrdersIT(Orders sut, Customers customers) {
		this.sut = sut;
		this.customers = customers;
	}

	@BeforeEach
	void setup() {
		if (!customers.exists(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)) {
			customers.add(CustomerTestBuilder.aCustomer().build());
		}
	}

	@Test
	public void shouldPersistAndFind() {
		Order originalOrder = OrderTestBuilder.aFilledDraftOrder().build();
		OrderId id = originalOrder.id();
		sut.add(originalOrder);

		Optional<Order> possibleOrder = sut.ofId(id);

		Assertions.assertTrue(possibleOrder.isPresent());

		Order savedOrder = possibleOrder.get();
		Assertions.assertEquals(originalOrder.id(), savedOrder.id());
		Assertions.assertEquals(originalOrder.customerId(), savedOrder.customerId());
		Assertions.assertEquals(originalOrder.status(), savedOrder.status());
	}

	@Test
	void shouldUpdateExistingOrder() {
		Order order = OrderTestBuilder
				.anExistingOrder()
				.withStatus(OrderStatus.PLACED).build();
		sut.add(order);

		// change the object for an equal (new) one
		order = sut.ofId(order.id()).orElseThrow();
		order.markAsPaid();

		sut.add(order);

		Order result = sut.ofId(order.id()).orElseThrow();
		Assertions.assertTrue(result.isPaid());
	}

	@Test
	@DisplayName("When an object is retrieved twice and one of them is changed and added, trying to save the second should throw OptimisticLockingFailureException")
	void givenDifferentObjectsOfSameEntityWithSameVersionShouldThrowOptimisticLockingFailureExceptionWhenUpdatingBoth() {
		OrderId id = new OrderId();
		Order order = OrderTestBuilder.aFilledDraftOrder()
		                              .withStatus(OrderStatus.PLACED)
		                              .withId(id)
		                              .build();
		sut.add(order);

		Order order1 = sut.ofId(id).orElseThrow();
		Order order2 = sut.ofId(id).orElseThrow();

		order1.markAsPaid();
		sut.add(order1);

		order2.cancel();
		Assertions.assertThrows(OptimisticLockingFailureException.class, () -> sut.add(order2));

		Order result = sut.ofId(id).orElseThrow();

		Assertions.assertNull(result.canceledAt());
		Assertions.assertNotNull(result.paidAt());
		Assertions.assertNotNull(result.version());
	}

	@Test
	void shouldSeeIfExistsAndCountExistingOrders() {
		Order order1 = OrderTestBuilder.aFilledDraftOrder()
		                              .withStatus(OrderStatus.PLACED)
		                              .build();

		sut.add(order1);

		Order order2 = OrderTestBuilder.aFilledDraftOrder()
		                               .build();

		sut.add(order2);

		Assertions.assertEquals(2, sut.count());
		Assertions.assertTrue(sut.exists(order1.id()));
	}

	@Test
	void shouldListExistingOrdersByYear() {
		Order orderStub1 = OrderTestBuilder.aFilledDraftOrder()
		                               .build();
		orderStub1.place();

		CustomerId differentCustomerId = new CustomerId();
		customers.add(CustomerTestBuilder
				.aCustomer()
	            .withId(differentCustomerId)
				.build());
		Order orderStub2 = OrderTestBuilder.aFilledDraftOrder()
				.withCustomerId(differentCustomerId)
		                                   .build();
		sut.add(orderStub1);
		sut.add(orderStub2);

		CustomerId customerId = CustomerTestBuilder.DEFAULT_CUSTOMER_ID;

		List<Order> result = sut.placedByCustomerInYear(customerId, Year.now());
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(1, result.size());

		List<Order> result2 = sut.placedByCustomerInYear(customerId, Year.now().minusYears(1));
		Assertions.assertTrue(result2.isEmpty());
	}

	@Test
	void shouldReturnTotalValueSoldForCustomer() {
		Order orderStub1 = OrderTestBuilder.aFilledDraftOrder().build();
		Order orderStub2 = OrderTestBuilder.aFilledDraftOrder().build();
		Order orderStub3 = OrderTestBuilder.aFilledDraftOrder().build();

		orderStub1.place();
		orderStub1.markAsPaid();
		orderStub2.place();
		orderStub2.markAsPaid();

		orderStub3.cancel();

		sut.add(orderStub1);
		sut.add(orderStub2);
		sut.add(orderStub3);

		Money expectedResult = orderStub1.totalAmount().add(orderStub2.totalAmount());
		Money actualResult = sut.totalSoldForCustomer(orderStub1.customerId());
		Assertions.assertEquals(expectedResult, actualResult);
	}


	@Test
	void shouldReturnNumberOfSalesPerCustomerPerYear() {
		Order orderStub1 = OrderTestBuilder.aFilledDraftOrder().build();
		Order orderStub2 = OrderTestBuilder.aFilledDraftOrder().build();
		Order orderStub3 = OrderTestBuilder.aFilledDraftOrder().build();

		orderStub1.place();
		orderStub1.markAsPaid();
		orderStub2.place();
		orderStub2.markAsPaid();

		sut.add(orderStub1);
		sut.add(orderStub2);
		sut.add(orderStub3);

		long expectedResult = 2L;
		long actualResult = sut.salesQuantityByCustomerInYear(orderStub1.customerId(), Year.now());

		Assertions.assertEquals(expectedResult, actualResult);
	}
}