package com.lutz.algashop.ordering.infrastructure.persistence.repository;

import com.lutz.algashop.ordering.infrastructure.persistence.builder.CustomerPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfiguration;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfiguration.class)
class CustomerPersistenceEntityRepositoryIT {

	private final CustomerPersistenceEntityRepository sut;

	@Autowired
	public CustomerPersistenceEntityRepositoryIT(CustomerPersistenceEntityRepository sut) {
		this.sut = sut;
	}

	@Test
	void shouldPersist() {
		CustomerPersistenceEntity customerPersistenceEntity = CustomerPersistenceEntityTestBuilder
				.existing()
				.build();

		sut.saveAndFlush(customerPersistenceEntity);
		Assertions.assertTrue(sut.existsById(customerPersistenceEntity.getId()));

		CustomerPersistenceEntity savedCustomer = sut.findById(customerPersistenceEntity.getId()).orElseThrow();

		assertEquals(customerPersistenceEntity.getId(), savedCustomer.getId());
		assertEquals(customerPersistenceEntity.getFirstName(), savedCustomer.getFirstName());
		assertEquals(customerPersistenceEntity.getLastName(), savedCustomer.getLastName());
	}

	@Test
	void shouldCount() {
		long customersCount = sut.count();
		assertEquals(0, customersCount);
	}

	@Test
	void shouldSetAuditingValues() {
		CustomerPersistenceEntity customerPersistenceEntity = CustomerPersistenceEntityTestBuilder.existing().build();
		CustomerPersistenceEntity result = sut.saveAndFlush(customerPersistenceEntity);

		assertNotNull(result.getCreatedByUserId());
		assertNotNull(result.getLastModifiedAt());
		assertNotNull(result.getLastModifiedUserId());
	}
}
