package com.lutz.algashop.ordering.application.customers.management;

import com.lutz.algashop.ordering.application.commons.AddressData;
import com.lutz.algashop.ordering.application.customer.management.CustomerInput;
import com.lutz.algashop.ordering.application.customer.management.CustomerManagementService;
import com.lutz.algashop.ordering.application.customer.management.CustomerOutput;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class CustomerManagementServiceIT {

	@Autowired
	private CustomerManagementService sut;

	@Test
	void shouldRegister() {
		Customer customerStub = CustomerTestBuilder.aCustomer().build();
		CustomerInput customer = CustomerInput.builder()
		                                      .firstName(customerStub.fullName().firstName())
		                                      .lastName(customerStub.fullName().lastName())
		                                      .email(customerStub.email().toString())
		                                      .phone(customerStub.phone().toString())
		                                      .document(customerStub.document().toString())
		                                      .birthdate(customerStub.birthdate().date())
		                                      .promotionNotificationsAllowed(customerStub.promotionNotificationAllowed())
		                                      .address(AddressData.fromDomain(customerStub.address()))
		                                      .build();
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
}