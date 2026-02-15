package com.lutz.algashop.ordering.infrastructure.persistence.disassembler;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.infrastructure.persistence.builder.OrderPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderPersistenceEntityDisassemblerIT {

	private final OrderItemPersistenceEntityDisassembler itemDisassembler = new OrderItemPersistenceEntityDisassembler();
	private OrderPersistenceEntityDisassembler sut;

	@BeforeEach
	void setup() {
		sut = new OrderPersistenceEntityDisassembler(itemDisassembler);
	}
	@Test
	void givenPersistenceObjectShouldConvertToDomainObject() {
		OrderPersistenceEntity entity = OrderPersistenceEntityTestBuilder.existing().build();

		Order result = sut.fromPersistence(entity);

		assertEquals(result.id(), new OrderId(entity.getId()));
		assertEquals(result.customerId(), new CustomerId(entity.getCustomerId()));
		assertEquals(result.totalAmount(), new Money(entity.getTotalAmount()));
		assertEquals(result.itemsAmount(), new Quantity(entity.getTotalItems()));
		assertEquals(result.status(), OrderStatus.valueOf(entity.getStatus()));
		assertEquals(result.paymentMethod(), PaymentMethod.valueOf(entity.getPaymentMethod()));
		assertEquals(result.placedAt(), entity.getPlacedAt());
		assertEquals(result.paidAt(), entity.getPaidAt());
		assertEquals(result.readyAt(), entity.getReadyAt());
		assertEquals(result.canceledAt(), entity.getCanceledAt());

		Long orderItemId = result.items().iterator().next().id().value().toLong();
		Long orderPersistenceEntityId = entity
				.getItems()
				.stream()
				.filter(itemEntity ->
						Objects.equals(itemEntity.getId(), orderItemId))
				.findFirst()
				.orElseThrow()
				.getId();

		assertEquals(orderPersistenceEntityId, orderItemId);
	}
}