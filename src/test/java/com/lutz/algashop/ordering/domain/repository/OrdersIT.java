package com.lutz.algashop.ordering.domain.repository;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.provider.OrdersPersistenceProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@DataJpaTest
@Import({OrdersPersistenceProvider.class, OrderPersistenceEntityAssembler.class, OrderPersistenceEntityDisassembler.class})
class OrdersIT {
	private final Orders sut;

	@Autowired
	public OrdersIT(Orders sut) {
		this.sut = sut;
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
}