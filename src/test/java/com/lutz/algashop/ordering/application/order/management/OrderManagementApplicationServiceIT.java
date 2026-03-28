package com.lutz.algashop.ordering.application.order.management;

import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFoundException;
import com.lutz.algashop.ordering.domain.order.exception.OrderStatusCannotBeChangedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderManagementApplicationServiceIT {

	@Autowired
	private OrderManagementApplicationService sut;

	@Autowired
	private Orders orders;

	@Autowired
	private Customers customers;

	@BeforeEach
	void setup() {
		if (!customers.exists(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)) {
			customers.add(CustomerTestBuilder.aCustomer().build());
		}
	}

	// cancel tests

	@Test
	void shouldCancelOrder() {
		var order = OrderTestBuilder.aFilledDraftOrder()
				.withStatus(OrderStatus.PLACED)
				.build();
		orders.add(order);

		sut.cancel(order.id().value().toLong());

		var updatedOrder = orders.ofId(order.id()).orElseThrow();
		assertEquals(OrderStatus.CANCELED, updatedOrder.status());
		assertNotNull(updatedOrder.canceledAt());
	}

	@Test
	void shouldThrowWhenCancellingNonExistentOrder() {
		var nonExistentOrderId = new OrderId().value().toLong();

		assertThrows(OrderNotFoundException.class,
				() -> sut.cancel(nonExistentOrderId));
	}

	@Test
	void shouldThrowWhenCancellingAlreadyCanceledOrder() {
		var order = OrderTestBuilder.aFilledDraftOrder()
				.withStatus(OrderStatus.CANCELED)
				.build();
		orders.add(order);

		assertThrows(OrderStatusCannotBeChangedException.class,
				() -> sut.cancel(order.id().value().toLong()));
	}

	// markAsPaid tests

	@Test
	void shouldMarkOrderAsPaid() {
		var order = OrderTestBuilder.aFilledDraftOrder()
				.withStatus(OrderStatus.PLACED)
				.build();
		orders.add(order);

		sut.markAsPaid(order.id().value().toLong());

		var updatedOrder = orders.ofId(order.id()).orElseThrow();
		assertEquals(OrderStatus.PAID, updatedOrder.status());
		assertNotNull(updatedOrder.paidAt());
	}

	@Test
	void shouldThrowWhenMarkingAsPaidNonExistentOrder() {
		var nonExistentOrderId = new OrderId().value().toLong();

		assertThrows(OrderNotFoundException.class,
				() -> sut.markAsPaid(nonExistentOrderId));
	}

	@Test
	void shouldThrowWhenMarkingAsPaidAlreadyPaidOrder() {
		var order = OrderTestBuilder.aFilledDraftOrder()
				.withStatus(OrderStatus.PAID)
				.build();
		orders.add(order);

		assertThrows(OrderStatusCannotBeChangedException.class,
				() -> sut.markAsPaid(order.id().value().toLong()));
	}

	// markAsReady tests

	@Test
	void shouldMarkOrderAsReady() {
		var order = OrderTestBuilder.aFilledDraftOrder()
				.withStatus(OrderStatus.PAID)
				.build();
		orders.add(order);

		sut.markAsReady(order.id().value().toLong());

		var updatedOrder = orders.ofId(order.id()).orElseThrow();
		assertEquals(OrderStatus.READY, updatedOrder.status());
		assertNotNull(updatedOrder.readyAt());
	}

	@Test
	void shouldThrowWhenMarkingAsReadyNonExistentOrder() {
		var nonExistentOrderId = new OrderId().value().toLong();

		assertThrows(OrderNotFoundException.class,
				() -> sut.markAsReady(nonExistentOrderId));
	}

	@Test
	void shouldThrowWhenMarkingAsReadyNotPaidOrder() {
		var order = OrderTestBuilder.aFilledDraftOrder()
				.withStatus(OrderStatus.PLACED)
				.build();
		orders.add(order);

		assertThrows(OrderStatusCannotBeChangedException.class,
				() -> sut.markAsReady(order.id().value().toLong()));
	}
}
