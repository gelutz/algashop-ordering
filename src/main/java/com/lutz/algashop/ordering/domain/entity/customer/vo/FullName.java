package com.lutz.algashop.ordering.domain.entity.customer.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import com.lutz.algashop.ordering.domain.validation.FieldValidator;

import java.util.Objects;

public record FullName(String firstName, String lastName) {
	public FullName(String firstName, String lastName) {
		Objects.requireNonNull(firstName, ErrorMessages.Validation.FULL_NAME_IS_NULL);

		FieldValidator.requireNonBlank(firstName, ErrorMessages.Validation.FULL_NAME_IS_BLANK);
		FieldValidator.requireNonBlank(lastName, ErrorMessages.Validation.FULL_NAME_IS_BLANK);

		this.firstName = firstName.trim();
		this.lastName = lastName.trim();
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
