package com.lutz.algashop.ordering.application.customer.loyalty;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.CustomerArchivedException;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.customer.CustomerNotFoundException;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.LoyaltyPoints;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFoundException;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFromThisCustomerException;
import com.lutz.algashop.ordering.domain.order.exception.WrongOrderStatusException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CustomerLoyaltyPointsApplicationServiceIT {

	@Autowired
	private CustomerLoyaltyPointsApplicationService sut;

	@Autowired
	private Customers customers;

	@Autowired
	private Orders orders;

	@Test
	void shouldAddLoyaltyPointsSuccessfully() {
		var customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		var order = OrderTestBuilder.aFilledDraftOrder()
				.withCustomerId(customer.id())
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("2000"))
				.withItemsAmount(new Quantity(1))
				.build();
		orders.add(order);

		sut.addLoyaltyPoints(customer.id().value(), order.id().toString());

		var updatedCustomer = customers.ofId(customer.id()).orElseThrow();
		assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(10));
	}

	@Test
	void shouldThrowWhenCustomerNotFound() {
		var customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		var order = OrderTestBuilder.aFilledDraftOrder()
				.withCustomerId(customer.id())
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("2000"))
				.withItemsAmount(new Quantity(1))
				.build();
		orders.add(order);

		assertThatThrownBy(() -> sut.addLoyaltyPoints(UUID.randomUUID(), order.id().toString()))
				.isInstanceOf(CustomerNotFoundException.class);
	}

	@Test
	void shouldThrowWhenOrderNotFound() {
		var customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		var nonExistentOrderId = new OrderId().toString();

		assertThatThrownBy(() -> sut.addLoyaltyPoints(customer.id().value(), nonExistentOrderId))
				.isInstanceOf(OrderNotFoundException.class);
	}

	@Test
	void shouldThrowWhenCustomerIsArchived() {
		var customer = CustomerTestBuilder.aCustomer().build();
		customer.archive();
		customers.add(customer);

		var order = OrderTestBuilder.aFilledDraftOrder()
				.withCustomerId(customer.id())
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("2000"))
				.withItemsAmount(new Quantity(1))
				.build();
		orders.add(order);

		assertThatThrownBy(() -> sut.addLoyaltyPoints(customer.id().value(), order.id().toString()))
				.isInstanceOf(CustomerArchivedException.class);
	}

	@Test
	void shouldThrowWhenOrderDoesNotBelongToCustomer() {
		var customerA = CustomerTestBuilder.aCustomer()
				.withId(new CustomerId())
				.build();
		customers.add(customerA);

		var customerB = CustomerTestBuilder.aCustomer()
				.withId(new CustomerId())
				.withEmail(new com.lutz.algashop.ordering.domain.commons.Email("customerb@email.com"))
				.withDocument(new com.lutz.algashop.ordering.domain.commons.Document("999999999"))
				.build();
		customers.add(customerB);

		var orderForB = OrderTestBuilder.aFilledDraftOrder()
				.withCustomerId(customerB.id())
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("2000"))
				.withItemsAmount(new Quantity(1))
				.build();
		orders.add(orderForB);

		assertThatThrownBy(() -> sut.addLoyaltyPoints(customerA.id().value(), orderForB.id().toString()))
				.isInstanceOf(OrderNotFromThisCustomerException.class);
	}

	@Test
	void shouldThrowWhenOrderIsNotReady() {
		var customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		var order = OrderTestBuilder.aFilledDraftOrder()
				.withCustomerId(customer.id())
				.withStatus(OrderStatus.PLACED)
				.withTotalAmount(new Money("2000"))
				.withItemsAmount(new Quantity(1))
				.build();
		orders.add(order);

		assertThatThrownBy(() -> sut.addLoyaltyPoints(customer.id().value(), order.id().toString()))
				.isInstanceOf(WrongOrderStatusException.class);
	}

	@Test
	void shouldNotAddPointsWhenOrderValueBelowThreshold() {
		var customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		var order = OrderTestBuilder.aFilledDraftOrder()
				.withCustomerId(customer.id())
				.withStatus(OrderStatus.READY)
				.withTotalAmount(new Money("500"))
				.withItemsAmount(new Quantity(1))
				.build();
		orders.add(order);

		sut.addLoyaltyPoints(customer.id().value(), order.id().toString());

		var updatedCustomer = customers.ofId(customer.id()).orElseThrow();
		assertThat(updatedCustomer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
	}
}
