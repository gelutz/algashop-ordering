package com.lutz.algashop.ordering.domain.service;

import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.Email;
import com.lutz.algashop.ordering.domain.entity.customer.vo.FullName;
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