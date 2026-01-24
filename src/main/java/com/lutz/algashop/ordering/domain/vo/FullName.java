package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;

import java.util.Objects;

public record FullName(String firstName, String lastName) {
	public FullName(String firstName, String lastName) {
		Objects.requireNonNull(firstName, ErrorMessages.Validation.FULL_NAME_IS_NULL);

		if (firstName.isBlank()) throw new IllegalArgumentException(ErrorMessages.Validation.FULL_NAME_IS_BLANK);
		if (lastName.isBlank()) throw new IllegalArgumentException(ErrorMessages.Validation.FULL_NAME_IS_BLANK);

		this.firstName = firstName.trim();
		this.lastName = lastName.trim();
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
