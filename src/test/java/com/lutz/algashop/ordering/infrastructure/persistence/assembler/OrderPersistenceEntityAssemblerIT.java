package com.lutz.algashop.ordering.infrastructure.persistence.assembler;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderPersistenceEntityAssemblerIT {
	private final OrderPersistenceEntityAssembler sut = new OrderPersistenceEntityAssembler();

	@Test
	void givenDomainObjectShouldConvertToPersistenceObject() {
		Order orderStub = OrderTestBuilder.aFilledDraftOrder().build();

		OrderPersistenceEntity result = sut.fromDomain(orderStub);

		assertEquals(result.getId(), orderStub.id().value().toLong());
		assertEquals(result.getCustomerId(), orderStub.customerId().value());
		assertEquals(result.getTotalAmount(), orderStub.totalAmount().value());
		assertEquals(result.getTotalItems(), orderStub.itemsAmount().value());
		assertEquals(result.getStatus(), orderStub.status().name());
		assertEquals(result.getPaymentMethod(), orderStub.paymentMethod().name());
		assertEquals(result.getPlacedAt(), orderStub.placedAt());
		assertEquals(result.getPaidAt(), orderStub.paidAt());
		assertEquals(result.getReadyAt(), orderStub.readyAt());
		assertEquals(result.getCanceledAt(), orderStub.canceledAt());
	}

	@Test
	void givenPersistenceObjectAndDomainObjectShouldMerge() {
		Order orderStub = OrderTestBuilder.aFilledDraftOrder().build();
		OrderPersistenceEntity base = new OrderPersistenceEntity();

		OrderPersistenceEntity result = sut.merge(base, orderStub);

		assertEquals(result.getId(), orderStub.id().value().toLong());
		assertEquals(result.getCustomerId(), orderStub.customerId().value());
		assertEquals(result.getTotalAmount(), orderStub.totalAmount().value());
		assertEquals(result.getTotalItems(), orderStub.itemsAmount().value());
		assertEquals(result.getStatus(), orderStub.status().name());
		assertEquals(result.getPaymentMethod(), orderStub.paymentMethod().name());
		assertEquals(result.getPlacedAt(), orderStub.placedAt());
		assertEquals(result.getPaidAt(), orderStub.paidAt());
		assertEquals(result.getReadyAt(), orderStub.readyAt());
		assertEquals(result.getCanceledAt(), orderStub.canceledAt());
	}
}