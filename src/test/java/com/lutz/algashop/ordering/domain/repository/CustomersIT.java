package com.lutz.algashop.ordering.domain.repository;


import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.customer.vo.Email;
import com.lutz.algashop.ordering.domain.entity.customer.vo.FullName;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.provider.CustomersPersistenceProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@Import({CustomersPersistenceProvider.class,
		CustomerPersistenceEntityAssembler.class,
		CustomerPersistenceEntityDisassembler.class})
class CustomersIT {

	private Customers customers;

	@Autowired
	public CustomersIT(Customers customers) {
		this.customers = customers;
	}

	@Test
	public void shouldPersistAndFind() {
		Customer originalCustomer = CustomerTestBuilder.aCustomer().build();
		CustomerId customerId = originalCustomer.id();
		customers.add(originalCustomer);

		Optional<Customer> possibleCustomer = customers.ofId(customerId);

		Assertions.assertTrue(possibleCustomer.isPresent());

		Customer savedCustomer = possibleCustomer.get();

		Assertions.assertEquals(customerId, savedCustomer.id());
	}

	@Test
	public void shouldUpdateExistingCustomer() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		customer = customers.ofId(customer.id()).orElseThrow();
		customer.archive();

		customers.add(customer);

		Customer savedCustomer = customers.ofId(customer.id()).orElseThrow();

		Assertions.assertNotNull(savedCustomer.archivedAt());
		Assertions.assertTrue(savedCustomer.archived());

	}

	@Test
	public void shouldNotAllowStaleUpdates() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		Customer customerT1 = customers.ofId(customer.id()).orElseThrow();
		Customer customerT2 = customers.ofId(customer.id()).orElseThrow();

		customerT1.archive();
		customers.add(customerT1);

		customerT2.changeName(new FullName("Alex","Silva"));

		Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> customers.add(customerT2));

		Customer savedCustomer = customers.ofId(customer.id()).orElseThrow();

		Assertions.assertNotNull(savedCustomer.archivedAt());
		Assertions.assertTrue(savedCustomer.archived());
	}

	@Test
	public void shouldCountExistingOrders() {
		Assertions.assertEquals(0, customers.count());

		Customer customer1 = CustomerTestBuilder
				.aCustomer()
				.withId(new CustomerId())
				.build();
		customers.add(customer1);

		Customer customer2 = CustomerTestBuilder
				.aCustomer()
				.withId(new CustomerId())
				.build();
		customers.add(customer2);

		Assertions.assertEquals(2, customers.count());
	}

	@Test
	public void shouldReturnValidateIfOrderExists() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		Assertions.assertTrue(customers.exists(customer.id()));
		Assertions.assertFalse(customers.exists(new CustomerId()));
	}

	@Test
	public void shouldFindByEmail() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		Optional<Customer> customerOptional = customers.ofEmail(customer.email());

		Assertions.assertTrue(customerOptional.isPresent());
	}

	@Test
	public void shouldNotFindByEmailIfNoCustomerExistsWithEmail() {
		Optional<Customer> customerOptional = customers
				.ofEmail(new Email(UUID.randomUUID() + "@email.com"));

		Assertions.assertFalse(customerOptional.isPresent());
	}

}