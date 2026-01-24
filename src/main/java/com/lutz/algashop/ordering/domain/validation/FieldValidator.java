package com.lutz.algashop.ordering.domain.validation;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import lombok.NonNull;
import org.apache.commons.validator.routines.EmailValidator;

public class FieldValidator {
	private FieldValidator() {}

	public static void requireValidEmail(String email) {
		requireValidEmail(email, ErrorMessages.Validation.EMAIL_IS_INVALID);
	}

	public static void requireValidEmail(@NonNull String email, String errorMessage) {
		if (email.isBlank()) throw new IllegalArgumentException();
		if (!EmailValidator.getInstance().isValid(email)) throw new IllegalArgumentException();
	}
}
