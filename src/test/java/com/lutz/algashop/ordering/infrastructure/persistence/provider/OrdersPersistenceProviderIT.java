package com.lutz.algashop.ordering.infrastructure.persistence.provider;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.OrderItemPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfiguration;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.OrderItemPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({
		OrderPersistenceEntityAssembler.class,
		OrderItemPersistenceEntityAssembler.class,
		OrderPersistenceEntityDisassembler.class,
		OrderItemPersistenceEntityDisassembler.class,
		OrdersPersistenceProvider.class,
		SpringDataAuditingConfiguration.class
})
class OrdersPersistenceProviderIT {
	private final OrdersPersistenceProvider sut;
	private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;

	@Autowired
	private OrdersPersistenceProviderIT(OrdersPersistenceProvider provider, OrderPersistenceEntityRepository repository) {
		this.sut = provider;
		this.orderPersistenceEntityRepository = repository;
	}

	@Test
	void givenDifferentObjectWithSameIdShouldUpdate() {
		Order order = OrderTestBuilder.aFilledDraftOrder().withStatus(OrderStatus.PLACED).build();
		sut.add(order);

		OrderPersistenceEntity result = orderPersistenceEntityRepository.findById(order.id().value().toLong()).orElseThrow();

		assertEquals(order.id().value().toLong(), result.getId());
		assertEquals(order.status().name(), result.getStatus());
		assertNotNull(result.getCreatedByUserId());
		assertNotNull(result.getLastModifiedAt());
		assertNotNull(result.getLastModifiedUserId());

		order.markAsPaid();
		sut.add(order);

		result = orderPersistenceEntityRepository.findById(order.id().value().toLong()).orElseThrow();

		assertEquals(order.status().name(), result.getStatus());
		assertNotNull(result.getCreatedByUserId());
		assertNotNull(result.getLastModifiedAt());
		assertNotNull(result.getLastModifiedUserId());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED) // forces to not be transactional
	void shouldNotFailWhenNoTransaction() {
		Order order = OrderTestBuilder.aFilledDraftOrder().build();
		sut.add(order);

		Assertions.assertDoesNotThrow(() -> sut.ofId(order.id()).orElseThrow());
	}
}