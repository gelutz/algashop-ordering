package com.lutz.algashop.ordering.domain.customer;


import com.lutz.algashop.ordering.domain.commons.Email;
import com.lutz.algashop.ordering.domain.commons.FullName;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomersPersistenceProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@Import({
	CustomersPersistenceProvider.class,
	CustomerPersistenceEntityAssembler.class,
	CustomerPersistenceEntityDisassembler.class,
})
class CustomersIT {

	private final Customers sut;

	@Autowired
	public CustomersIT(Customers customers) {
		this.sut = customers;
	}

	@Test
	public void shouldPersistAndFind() {
		Customer originalCustomer = CustomerTestBuilder.aCustomer().build();
		CustomerId customerId = originalCustomer.id();
		sut.add(originalCustomer);

		Optional<Customer> possibleCustomer = sut.ofId(customerId);

		Assertions.assertTrue(possibleCustomer.isPresent());

		Customer savedCustomer = possibleCustomer.get();

		Assertions.assertEquals(customerId, savedCustomer.id());
	}

	@Test
	public void shouldUpdateExistingCustomer() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		sut.add(customer);

		customer = sut.ofId(customer.id()).orElseThrow();
		customer.archive();

		sut.add(customer);

		Customer savedCustomer = sut.ofId(customer.id()).orElseThrow();

		Assertions.assertNotNull(savedCustomer.archivedAt());
		Assertions.assertTrue(savedCustomer.archived());

	}

	@Test
	public void shouldNotAllowStaleUpdates() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		sut.add(customer);

		Customer customerT1 = sut.ofId(customer.id()).orElseThrow();
		Customer customerT2 = sut.ofId(customer.id()).orElseThrow();

		customerT1.archive();
		sut.add(customerT1);

		customerT2.changeName(new FullName("Alex","Silva"));

		Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> sut.add(customerT2));

		Customer savedCustomer = sut.ofId(customer.id()).orElseThrow();

		Assertions.assertNotNull(savedCustomer.archivedAt());
		Assertions.assertTrue(savedCustomer.archived());
	}

	@Test
	public void shouldCountExistingOrders() {
		Assertions.assertEquals(0, sut.count());

		Customer customer1 = CustomerTestBuilder
				.aCustomer()
				.withId(new CustomerId())
				.build();
		sut.add(customer1);

		Customer customer2 = CustomerTestBuilder
				.aCustomer()
				.withId(new CustomerId())
				.build();
		sut.add(customer2);

		Assertions.assertEquals(2, sut.count());
	}

	@Test
	public void shouldReturnValidateIfOrderExists() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		sut.add(customer);

		Assertions.assertTrue(sut.exists(customer.id()));
		Assertions.assertFalse(sut.exists(new CustomerId()));
	}

	@Test
	public void shouldFindByEmail() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		sut.add(customer);

		Optional<Customer> customerOptional = sut.ofEmail(customer.email());

		Assertions.assertTrue(customerOptional.isPresent());
	}

	@Test
	public void shouldNotFindByEmailIfNoCustomerExistsWithEmail() {
		Optional<Customer> customerOptional = sut
				.ofEmail(new Email(UUID.randomUUID() + "@email.com"));

		Assertions.assertFalse(customerOptional.isPresent());
	}

	@Test
	void shouldReturnTrueIfEmailIsInUse() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		Customer customer2 = CustomerTestBuilder
				.aCustomer()
				.withId(new CustomerId())
				.withEmail(new Email("different@email.com"))
				.build();

		sut.add(customer);
		sut.add(customer2);

		Assertions.assertTrue(sut.isEmailUnique(customer.email(), customer.id()));
	}
}