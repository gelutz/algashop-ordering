package com.lutz.algashop.ordering.infrastructure.persistence.provider;

import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.customer.vo.FullName;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfiguration;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({
		CustomerPersistenceEntityAssembler.class,
		CustomerPersistenceEntityDisassembler.class,
		CustomersPersistenceProvider.class,
		SpringDataAuditingConfiguration.class
})
class CustomersPersistenceProviderIT {
	private final CustomersPersistenceProvider sut;
	private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

	@Autowired
	private CustomersPersistenceProviderIT(CustomersPersistenceProvider provider, 
			CustomerPersistenceEntityRepository repository,
			EntityManager entityManager) {
		this.sut = provider;
		this.customerPersistenceEntityRepository = repository;
	}

	@Test
	void givenDifferentObjectWithSameIdShouldUpdate() {
		Customer customer = CustomerTestBuilder.aCustomer()
		                                       .withId(new CustomerId())
		                                       .build();
		sut.add(customer);

		CustomerPersistenceEntity result = customerPersistenceEntityRepository.findById(customer.id().value()).orElseThrow();

		assertEquals(customer.id().value(), result.getId());
		assertEquals(customer.fullName().firstName(), result.getFirstName());
		assertNotNull(result.getCreatedByUserId());
		assertNotNull(result.getLastModifiedAt());
		assertNotNull(result.getLastModifiedUserId());

		customer.changeName(new FullName("Jane", "Smith"));
		sut.add(customer);

		result = customerPersistenceEntityRepository.findById(customer.id().value()).orElseThrow();

		assertEquals("Jane", result.getFirstName());
		assertEquals("Smith", result.getLastName());
		assertNotNull(result.getCreatedByUserId());
		assertNotNull(result.getLastModifiedAt());
		assertNotNull(result.getLastModifiedUserId());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	void shouldNotFailWhenNoTransaction() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		sut.add(customer);

		Assertions.assertDoesNotThrow(() -> sut.ofId(customer.id()).orElseThrow());
	}

	@Test
	void shouldThrowOptimisticLockingExceptionWhenUpdatingWithOldVersion() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		sut.add(customer);

		CustomerPersistenceEntity persistedCustomer = customerPersistenceEntityRepository
				.findById(customer.id().value())
				.orElseThrow();

		persistedCustomer.setFirstName("Updated Name");
		customerPersistenceEntityRepository.saveAndFlush(persistedCustomer);

		customer.changeName(new FullName("New Name", "LastName"));

		Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
			sut.add(customer);
		});
	}
}
