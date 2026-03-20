package com.lutz.algashop.ordering.application.customer.management.builder;

import com.lutz.algashop.ordering.application.commons.AddressData;
import com.lutz.algashop.ordering.application.customer.management.CustomerInput;

import java.time.LocalDate;

public class CustomerInputTestBuilder {

	private String firstName = "John";
	private String lastName = "Doe";
	private String email = "valid@email.com";
	private String phone = "123123123";
	private String document = "123123123";
	private LocalDate birthdate = LocalDate.now().minusYears(10);
	private Boolean promotionNotificationsAllowed = false;
	private AddressData address = AddressData.builder()
	                                         .street("This street")
	                                         .number("102")
	                                         .complement("near the thing")
	                                         .neighborhood("that one")
	                                         .city("Brooklyn")
	                                         .state("New York")
	                                         .zipCode("123123")
	                                         .build();

	private CustomerInputTestBuilder() {
	}

	public static CustomerInputTestBuilder aCustomerInput() {
		return new CustomerInputTestBuilder();
	}

	public CustomerInputTestBuilder withFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public CustomerInputTestBuilder withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public CustomerInputTestBuilder withEmail(String email) {
		this.email = email;
		return this;
	}

	public CustomerInputTestBuilder withPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public CustomerInputTestBuilder withDocument(String document) {
		this.document = document;
		return this;
	}

	public CustomerInputTestBuilder withBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
		return this;
	}

	public CustomerInputTestBuilder withPromotionNotificationsAllowed(Boolean promotionNotificationsAllowed) {
		this.promotionNotificationsAllowed = promotionNotificationsAllowed;
		return this;
	}

	public CustomerInputTestBuilder withAddress(AddressData address) {
		this.address = address;
		return this;
	}

	public CustomerInput build() {
		return CustomerInput.builder()
		                    .firstName(firstName)
		                    .lastName(lastName)
		                    .email(email)
		                    .phone(phone)
		                    .document(document)
		                    .birthdate(birthdate)
		                    .promotionNotificationsAllowed(promotionNotificationsAllowed)
		                    .address(address)
		                    .build();
	}
}
