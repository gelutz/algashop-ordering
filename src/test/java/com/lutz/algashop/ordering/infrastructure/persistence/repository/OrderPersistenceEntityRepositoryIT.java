package com.lutz.algashop.ordering.infrastructure.persistence.repository;

import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.utils.IdGenerator;
import com.lutz.algashop.ordering.infrastructure.persistence.builder.CustomerPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.builder.OrderPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfiguration;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // faz o @DataJpaTest usar o banco que configuramos no application-test.yaml
@Import(SpringDataAuditingConfiguration.class)
class OrderPersistenceEntityRepositoryIT {

	private final OrderPersistenceEntityRepository sut;
	private final CustomerPersistenceEntityRepository customerRepository;

	private CustomerPersistenceEntity defaultCustomer;

	@Autowired
	public OrderPersistenceEntityRepositoryIT(OrderPersistenceEntityRepository sut, CustomerPersistenceEntityRepository customerRepository) {
		this.sut = sut;
		this.customerRepository = customerRepository;
	}

	@BeforeEach
	void setup() {
		if (!customerRepository.existsById(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value())) {
			defaultCustomer = customerRepository.saveAndFlush(CustomerPersistenceEntityTestBuilder.existing().build());
		}
	}

	@Test
	void shouldPersist() {
		Long id = IdGenerator.generateTSID().toLong();
		OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestBuilder
				.existing()
				.id(id)
				.customer(defaultCustomer)
				.build();

		sut.saveAndFlush(orderPersistenceEntity);
		Assertions.assertTrue(sut.existsById(id));

		OrderPersistenceEntity savedOrder = sut.findById(id).orElseThrow();

		Assertions.assertFalse(savedOrder.getItems().isEmpty());
	}

	@Test
	void shouldCount() {
		long ordersCount = sut.count();
		assertEquals(0, ordersCount);
	}

	@Test
	void shouldSetAuditingValues() {
	OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestBuilder
			.existing()
			.customer(defaultCustomer)
			.build();
		OrderPersistenceEntity result = sut.saveAndFlush(orderPersistenceEntity);

		assertNotNull(result.getCreatedByUserId());
		assertNotNull(result.getLastModifiedAt());
		assertNotNull(result.getLastModifiedUserId());
	}
}