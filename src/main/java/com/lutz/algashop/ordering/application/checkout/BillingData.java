package com.lutz.algashop.ordering.application.checkout;

import com.lutz.algashop.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingData {
	private String firstName;
	private String lastName;
	private String document;
	private String phone;
	private String email;
	private AddressData address;
}
