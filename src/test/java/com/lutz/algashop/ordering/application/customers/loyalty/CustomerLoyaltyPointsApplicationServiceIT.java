package com.lutz.algashop.ordering.application.customers.loyalty;

import com.lutz.algashop.ordering.application.customer.loyalty.CustomerLoyaltyPointsApplicationService;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.CustomerArchivedException;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.customer.CustomerNotFoundException;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFoundException;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFromThisCustomerException;
import com.lutz.algashop.ordering.domain.order.exception.WrongOrderStatusException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import static com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder.DEFAULT_CUSTOMER_ID;
import static com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder.aCustomer;
import static com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder.aFilledDraftOrder;

@SpringBootTest
@Transactional
class CustomerLoyaltyPointsApplicationServiceIT {

	@Autowired
	private CustomerLoyaltyPointsApplicationService sut;

	@Autowired
	private Customers customers;

	@Autowired
	private Orders orders;

	@BeforeEach
	void setup() {
		if (!customers.exists(DEFAULT_CUSTOMER_ID)) {
			customers.add(aCustomer().build());
		}
	}

	@Test
	void shouldAddLoyaltyPoints() {
		Order order = aFilledDraftOrder()
				.withCustomerId(DEFAULT_CUSTOMER_ID)
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("5000"))
				.build();
		orders.add(order);

		sut.addLoyaltyPoints(DEFAULT_CUSTOMER_ID.value(), order.id().toString());

		Customer customer = customers.ofId(DEFAULT_CUSTOMER_ID).orElseThrow();
		Assertions.assertEquals(25, customer.loyaltyPoints().value());
	}

	@Test
	void shouldThrowWhenCustomerNotFound() {
		Order order = aFilledDraftOrder()
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("5000"))
				.build();
		orders.add(order);

		UUID nonExistentCustomerId = UUID.randomUUID();

		Assertions.assertThrows(CustomerNotFoundException.class,
				() -> sut.addLoyaltyPoints(nonExistentCustomerId, order.id().toString()));
	}

	@Test
	void shouldThrowWhenOrderNotFound() {
		Assertions.assertThrows(OrderNotFoundException.class,
				() -> sut.addLoyaltyPoints(DEFAULT_CUSTOMER_ID.value(), new OrderId().toString()));
	}

	@Test
	void shouldThrowWhenCustomerIsArchived() {
		CustomerId archivedCustomerId = new CustomerId();
		Customer archivedCustomer = aCustomer()
				.withId(archivedCustomerId)
				.withEmail(new com.lutz.algashop.ordering.domain.commons.Email("archived@test.com"))
				.withDocument(new com.lutz.algashop.ordering.domain.commons.Document("999999999"))
				.build();
		archivedCustomer.archive();
		customers.add(archivedCustomer);

		Order order = aFilledDraftOrder()
				.withCustomerId(archivedCustomerId)
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("5000"))
				.build();
		orders.add(order);

		Assertions.assertThrows(CustomerArchivedException.class,
				() -> sut.addLoyaltyPoints(archivedCustomerId.value(), order.id().toString()));
	}

	@Test
	void shouldThrowWhenOrderDoesNotBelongToCustomer() {
		CustomerId otherCustomerId = new CustomerId();
		Customer otherCustomer = aCustomer()
				.withId(otherCustomerId)
				.withEmail(new com.lutz.algashop.ordering.domain.commons.Email("other@test.com"))
				.withDocument(new com.lutz.algashop.ordering.domain.commons.Document("888888888"))
				.build();
		customers.add(otherCustomer);

		Order order = aFilledDraftOrder()
				.withCustomerId(otherCustomerId)
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("5000"))
				.build();
		orders.add(order);

		Assertions.assertThrows(OrderNotFromThisCustomerException.class,
				() -> sut.addLoyaltyPoints(DEFAULT_CUSTOMER_ID.value(), order.id().toString()));
	}

	@Test
	void shouldThrowWhenOrderIsNotReady() {
		Order order = aFilledDraftOrder()
				.withCustomerId(DEFAULT_CUSTOMER_ID)
				.withStatus(OrderStatus.PLACED)
				.withTotalAmount(new Money("5000"))
				.build();
		orders.add(order);

		Assertions.assertThrows(WrongOrderStatusException.class,
				() -> sut.addLoyaltyPoints(DEFAULT_CUSTOMER_ID.value(), order.id().toString()));
	}

	@Test
	void shouldNotAddPointsWhenBelowThreshold() {
		Order order = aFilledDraftOrder()
				.withCustomerId(DEFAULT_CUSTOMER_ID)
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("500"))
				.build();
		orders.add(order);

		sut.addLoyaltyPoints(DEFAULT_CUSTOMER_ID.value(), order.id().toString());

		Customer customer = customers.ofId(DEFAULT_CUSTOMER_ID).orElseThrow();
		Assertions.assertEquals(0, customer.loyaltyPoints().value());
	}
}
