package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.commons.Email;
import com.lutz.algashop.ordering.domain.commons.FullName;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


// this test is supposed to test @DomainService annotation
@SpringBootTest
class CustomerRegistrationServiceTest {

	@Autowired
	private CustomerRegistrationService sut;


	@Test
	void givenUniqueEmailShouldRegisterCustomer() {
		Customer baseCustomer = CustomerTestBuilder.aCustomer().build();
		Email baseEmail = baseCustomer.email();
		FullName baseName = baseCustomer.fullName();
		Customer result = sut.register(
				baseName,
				baseCustomer.birthdate(),
				baseEmail,
				baseCustomer.phone(),
				baseCustomer.document(),
				baseCustomer.address(),
				baseCustomer.promotionNotificationAllowed()
        );

		Assertions.assertEquals(baseEmail, result.email());
		Assertions.assertEquals(baseName, result.fullName());

	}
}