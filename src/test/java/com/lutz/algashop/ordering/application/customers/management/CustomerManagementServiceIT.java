package com.lutz.algashop.ordering.application.customers.management;

import com.lutz.algashop.ordering.application.commons.AddressData;
import com.lutz.algashop.ordering.application.customer.management.CustomerManagementService;
import com.lutz.algashop.ordering.application.customer.management.CustomerOutput;
import com.lutz.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.lutz.algashop.ordering.domain.customer.CustomerNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.lutz.algashop.ordering.application.customer.management.builder.CustomerInputTestBuilder.aCustomerInput;
import static com.lutz.algashop.ordering.application.customer.management.builder.CustomerUpdateInputTestBuilder.aCustomerUpdateInput;

@SpringBootTest
@Transactional
class CustomerManagementServiceIT {

	@Autowired
	private CustomerManagementService sut;

	@Test
	void shouldRegister() {
		var customer = aCustomerInput().build();
		UUID resultingId = sut.create(customer);

		Assertions.assertNotNull(resultingId);

		CustomerOutput resultingOutput = sut.findById(resultingId);

		Assertions.assertEquals(customer.getFirstName(), resultingOutput.getFirstName());
		Assertions.assertEquals(customer.getLastName(), resultingOutput.getLastName());
		Assertions.assertEquals(customer.getEmail(), resultingOutput.getEmail());
		Assertions.assertEquals(customer.getPhone(), resultingOutput.getPhone());
		Assertions.assertEquals(customer.getDocument(), resultingOutput.getDocument());
		Assertions.assertEquals(customer.getBirthdate(), resultingOutput.getBirthdate());
	}

	@Test
	void shouldUpdateCustomer() {
		UUID customerId = sut.create(aCustomerInput().build());

		CustomerUpdateInput updateInput = aCustomerUpdateInput()
				.withFirstName("Jane")
				.withLastName("Smith")
				.withPhone("999888777")
				.withPromotionNotificationsAllowed(true)
				.withAddress(AddressData.builder()
				                        .street("New street")
				                        .number("200")
				                        .complement("apt 5")
				                        .neighborhood("downtown")
				                        .city("Manhattan")
				                        .state("New York")
				                        .zipCode("456456")
				                        .build())
				.build();

		sut.update(customerId, updateInput);

		CustomerOutput updated = sut.findById(customerId);

		Assertions.assertEquals("Jane", updated.getFirstName());
		Assertions.assertEquals("Smith", updated.getLastName());
		Assertions.assertEquals("999888777", updated.getPhone());
		Assertions.assertEquals(true, updated.getPromotionNotificationAllowed());
		Assertions.assertEquals("New street", updated.getAddress().getStreet());
		Assertions.assertEquals("200", updated.getAddress().getNumber());
		Assertions.assertEquals("apt 5", updated.getAddress().getComplement());
		Assertions.assertEquals("downtown", updated.getAddress().getNeighborhood());
		Assertions.assertEquals("Manhattan", updated.getAddress().getCity());
		Assertions.assertEquals("New York", updated.getAddress().getState());
		Assertions.assertEquals("456456", updated.getAddress().getZipCode());
	}

	@Test
	void shouldThrowWhenUpdatingNonExistentCustomer() {
		CustomerUpdateInput updateInput = aCustomerUpdateInput().build();

		Assertions.assertThrows(CustomerNotFoundException.class,
				() -> sut.update(UUID.randomUUID(), updateInput));
	}
}
