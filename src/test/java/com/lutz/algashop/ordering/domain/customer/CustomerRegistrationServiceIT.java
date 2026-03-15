package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.commons.Email;
import com.lutz.algashop.ordering.domain.commons.FullName;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
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