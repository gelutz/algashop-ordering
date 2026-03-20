package com.lutz.algashop.ordering.application.customer.management.builder;

import com.lutz.algashop.ordering.application.commons.AddressData;
import com.lutz.algashop.ordering.application.customer.management.CustomerUpdateInput;

public class CustomerUpdateInputTestBuilder {

	private String firstName = "John";
	private String lastName = "Doe";
	private String phone = "123123123";
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

	private CustomerUpdateInputTestBuilder() {
	}

	public static CustomerUpdateInputTestBuilder aCustomerUpdateInput() {
		return new CustomerUpdateInputTestBuilder();
	}

	public CustomerUpdateInputTestBuilder withFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public CustomerUpdateInputTestBuilder withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public CustomerUpdateInputTestBuilder withPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public CustomerUpdateInputTestBuilder withPromotionNotificationsAllowed(Boolean promotionNotificationsAllowed) {
		this.promotionNotificationsAllowed = promotionNotificationsAllowed;
		return this;
	}

	public CustomerUpdateInputTestBuilder withAddress(AddressData address) {
		this.address = address;
		return this;
	}

	public CustomerUpdateInput build() {
		return CustomerUpdateInput.builder()
		                          .firstName(firstName)
		                          .lastName(lastName)
		                          .phone(phone)
		                          .promotionNotificationsAllowed(promotionNotificationsAllowed)
		                          .address(address)
		                          .build();
	}
}
