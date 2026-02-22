package com.lutz.algashop.ordering.domain.service;

import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.customer.vo.Email;
import com.lutz.algashop.ordering.domain.entity.customer.vo.FullName;
import com.lutz.algashop.ordering.domain.repository.Customers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRegistrationServiceIT {

	@Mock
	private Customers customers;

	@InjectMocks
	private CustomerRegistrationService sut;


	@Test
	void givenUniqueEmailShouldRegisterCustomer() {
		when(customers.isEmailUnique(any(Email.class), any(CustomerId.class))).thenReturn(true);

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