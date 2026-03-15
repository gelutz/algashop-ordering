package com.lutz.algashop.ordering.application.customer.management;

import com.lutz.algashop.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInput {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String document;
	private LocalDate birthdate;
	private Boolean promotionNotificationsAllowed;
	private AddressData address;
}
