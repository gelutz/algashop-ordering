package com.lutz.algashop.ordering.application.customer.query;

import com.lutz.algashop.ordering.application.commons.AddressData;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerOutputTestBuilder {

	public static CustomerOutput.CustomerOutputBuilder existing() {
		return CustomerOutput.builder()
		                     .id(UUID.randomUUID())
		                     .registeredAt(OffsetDateTime.now())
		                     .phone("1191234564")
		                     .email("johndoe@email.com")
		                     .firstName("John")
		                     .lastName("Doe")
		                     .birthdate(LocalDate.of(1991, 7, 5))
		                     .document("12345")
		                     .promotionNotificationAllowed(false)
		                     .loyaltyPoints(0)
		                     .archived(false)
		                     .address(AddressData.builder()
		                                         .street("Bourbon Street")
		                                         .number("2000")
		                                         .complement("apt 122")
		                                         .neighborhood("North Ville")
		                                         .city("Yostfort")
		                                         .state("South Carolina")
		                                         .zipCode("12321")
		                                         .build());
	}

}
