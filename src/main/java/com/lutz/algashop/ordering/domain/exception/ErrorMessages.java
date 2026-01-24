package com.lutz.algashop.ordering.domain.exception;

public class ErrorMessages {
	public static class Validation {
		public static final String INVALID_EMAIL = "Validation - Email is invalid.";

		public static final String BIRTHDATE_MUST_NOT_BE_NULL = "Validation - Birthdate must not be null.";
		public static final String BIRTHDATE_MUST_IN_PAST = "Validation - Birthdate must be a past date.";

		public static final String FULL_NAME_IS_NULL = "Validation - FullName cannot be null.";
		public static final String FULL_NAME_IS_BLANK = "Validation - FullName cannot be blank.";

		public static final String EMAIL_IS_INVALID = "Validation - Email is invalid.";
	}

	public static final String CUSTOMER_ARCHIVED = "This customer is already archived.";
}
