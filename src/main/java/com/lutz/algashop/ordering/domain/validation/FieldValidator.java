package com.lutz.algashop.ordering.domain.validation;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class FieldValidator {
	private FieldValidator() {}

	public static void requireValidEmail(String email) {
		Objects.requireNonNull(email, ErrorMessages.Fields.EMAIL_IS_NULL);
		requireNonBlank(email, ErrorMessages.Fields.EMAIL_IS_BLANK);
		if (!EmailValidator.getInstance().isValid(email)) throw new IllegalArgumentException(ErrorMessages.Fields.EMAIL_IS_INVALID);
	}

	public static void requireNonBlank(String value) {
		if (value.isBlank()) throw new IllegalArgumentException();
	}

	public static void requireNonBlank(String value, String errorMessage) {
		if (value.isBlank()) throw new IllegalArgumentException(errorMessage);
	}
}
