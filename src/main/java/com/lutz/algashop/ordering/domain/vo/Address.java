package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.validation.FieldValidator;
import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record Address(
		@NonNull String street,
		@NonNull String number,
		String complement,
		@NonNull String neighborhood,
		@NonNull String city,
		@NonNull String state,
		@NonNull ZipCode zipCode
) {
	public Address {
		FieldValidator.requireNonBlank(street);
		FieldValidator.requireNonBlank(number);
		FieldValidator.requireNonBlank(neighborhood);
		FieldValidator.requireNonBlank(city);
		FieldValidator.requireNonBlank(state);
	}
}
