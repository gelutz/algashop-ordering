package com.lutz.algashop.ordering.infrastructure.persistence.order;

import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomersPersistenceProvider;
import com.lutz.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfiguration;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntityDisassembler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
		SpringDataAuditingConfiguration.class,
		CustomersPersistenceProvider.class,
		CustomerPersistenceEntityAssembler.class,
		CustomerPersistenceEntityDisassembler.class
})
class OrdersPersistenceProviderIT {
	private final OrdersPersistenceProvider sut;
	private final Customers customers;
	private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;

	@Autowired
	private OrdersPersistenceProviderIT(OrdersPersistenceProvider provider, OrderPersistenceEntityRepository repository, CustomersPersistenceProvider customers) {
		this.sut = provider;
		this.orderPersistenceEntityRepository = repository;
		this.customers = customers;
	}

	@BeforeEach
	void setup() {
		if (!customers.exists(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)) {
			customers.add(CustomerTestBuilder.aCustomer().build());
		}
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