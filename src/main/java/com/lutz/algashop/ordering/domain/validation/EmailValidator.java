package com.lutz.algashop.ordering.domain.validation;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import lombok.NonNull;

public class EmailValidator {
	private EmailValidator() {}

	public static void requireValidEmail(String email) {
		requireValidEmail(email, ErrorMessages.Validation.EMAIL_IS_INVALID);
	}

	public static void requireValidEmail(@NonNull String email, String errorMessage) {
		if (email.isBlank()) throw new IllegalArgumentException();
		if (!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email)) throw new IllegalArgumentException();
	}
}
