package com.lutz.algashop.ordering.application.order.management;

import com.lutz.algashop.ordering.application.customer.loyalty.CustomerLoyaltyPointsApplicationService;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.OrderCanceledEvent;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.OrderPaidEvent;
import com.lutz.algashop.ordering.domain.order.OrderReadyEvent;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFoundException;
import com.lutz.algashop.ordering.domain.order.exception.OrderStatusCannotBeChangedException;
import com.lutz.algashop.ordering.infrastructure.listener.order.OrderEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

	@MockitoSpyBean
	private OrderEventListener orderEventListener;

	@MockitoSpyBean
	private CustomerLoyaltyPointsApplicationService loyaltyPointsApplicationService;

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
				.build();

		order.place();
		orders.add(order);

		sut.cancel(order.id().value().toLong());

		var updatedOrder = orders.ofId(order.id()).orElseThrow();
		assertEquals(OrderStatus.CANCELED, updatedOrder.status());
		assertNotNull(updatedOrder.canceledAt());
		Mockito.verify(orderEventListener, Mockito.times(1))
				.listen(Mockito.any(OrderCanceledEvent.class));
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
				.build();

		order.cancel();
		orders.add(order);

		assertThrows(OrderStatusCannotBeChangedException.class,
				() -> sut.cancel(order.id().value().toLong()));
	}

	// markAsPaid tests

	@Test
	void shouldMarkOrderAsPaid() {
		var order = OrderTestBuilder.aFilledDraftOrder()
				.build();

		order.place();
		orders.add(order);

		sut.markAsPaid(order.id().value().toLong());

		var updatedOrder = orders.ofId(order.id()).orElseThrow();
		assertEquals(OrderStatus.PAID, updatedOrder.status());
		assertNotNull(updatedOrder.paidAt());
		Mockito.verify(orderEventListener, Mockito.times(1))
				.listen(Mockito.any(OrderPaidEvent.class));
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
				.build();

		order.place();
		order.markAsPaid();
		orders.add(order);

		assertThrows(OrderStatusCannotBeChangedException.class,
				() -> sut.markAsPaid(order.id().value().toLong()));
	}

	// markAsReady tests

	@Test
	void shouldMarkOrderAsReady() {
		var order = OrderTestBuilder.aFilledDraftOrder()
				.build();

		order.place();
		order.markAsPaid();
		orders.add(order);

		sut.markAsReady(order.id().value().toLong());

		var updatedOrder = orders.ofId(order.id()).orElseThrow();
		assertEquals(OrderStatus.READY, updatedOrder.status());
		assertNotNull(updatedOrder.readyAt());
		Mockito.verify(orderEventListener, Mockito.times(1))
				.listen(Mockito.any(OrderReadyEvent.class));
		Mockito.verify(loyaltyPointsApplicationService).addLoyaltyPoints(Mockito.any(UUID.class), Mockito.any(String.class));
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
				.build();

		order.place();
		orders.add(order);

		assertThrows(OrderStatusCannotBeChangedException.class,
				() -> sut.markAsReady(order.id().value().toLong()));
	}
}
