package com.lutz.algashop.ordering.application.customer.management;

import com.lutz.algashop.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateInput {
	private String firstName;
	private String lastName;
	private String phone;
	private Boolean promotionNotificationsAllowed;
	private AddressData address;
}
