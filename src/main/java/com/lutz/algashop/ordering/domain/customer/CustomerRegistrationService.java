package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.DomainService;
import com.lutz.algashop.ordering.domain.commons.*;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class CustomerRegistrationService {
	private final Customers customers;

	public Customer register(
			FullName fullName, Birthdate birthDate, Email email, Phone phone, Document document, Address address, Boolean promotionNotificationAllowed
	                        ) {

		verifyEmail(email, new CustomerId());

		return Customer.fresh()
		                         .fullName(fullName)
		                         .birthDate(birthDate)
		                         .email(email)
		                         .phone(phone)
		                         .document(document)
		                         .address(address)
		                         .promotionNotificationAllowed(promotionNotificationAllowed)
		                         .build();
	}

	public void changeEmail(Email newEmail, Customer customer) {
		verifyEmail(newEmail, customer.id());
		customer.changeEmail(newEmail);
	}

	private void verifyEmail(Email email, CustomerId customerId) {
		if (!customers.isEmailUnique(email, customerId)) {
			throw new CustomerEmailIsInUseException();
		}
	}
}
