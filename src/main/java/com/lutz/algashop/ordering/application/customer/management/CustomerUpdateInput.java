package com.lutz.algashop.ordering.application.customer.management;

import com.lutz.algashop.ordering.application.commons.AddressData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateInput {
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String phone;
	@NotNull
	private Boolean promotionNotificationsAllowed;
	@Valid
	@NotNull
	private AddressData address;
}
