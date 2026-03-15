package com.lutz.algashop.ordering.domain.commons;

import com.lutz.algashop.ordering.domain.FieldValidator;

import java.util.Objects;

public record FullName(String firstName, String lastName) {
	public FullName(String firstName, String lastName) {
		Objects.requireNonNull(firstName, ErrorMessages.Fields.FULL_NAME_IS_NULL);

		FieldValidator.requireNonBlank(firstName, ErrorMessages.Fields.FULL_NAME_IS_BLANK);
		FieldValidator.requireNonBlank(lastName, ErrorMessages.Fields.FULL_NAME_IS_BLANK);

		this.firstName = firstName.trim();
		this.lastName = lastName.trim();
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
