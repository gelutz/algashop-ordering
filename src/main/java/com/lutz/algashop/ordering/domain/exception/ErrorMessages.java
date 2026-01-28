package com.lutz.algashop.ordering.domain.exception;

public class ErrorMessages {
	public static class Validation {
		public static String fieldIsNullMessage(String field) {
			return field + " is null.";
		}

		public static final String EMAIL_IS_NULL = fieldIsNullMessage("Email");
		public static final String EMAIL_IS_BLANK = "Email is blank.";
		public static final String EMAIL_IS_INVALID = "Email is invalid.";

		public static final String FULL_NAME_IS_NULL = "FullName cannot be null.";
		public static final String FULL_NAME_IS_BLANK = "FullName cannot be blank.";

		public static final String BIRTHDATE_IS_NULL = "Birthdate must not be null.";
		public static final String BIRTHDATE_IS_IN_FUTURE = "Birthdate must be a past date.";

		public static final String DOCUMENT_IS_NULL = "Document cannot be null.";
		public static final String DOCUMENT_IS_BLANK = "Document cannot be blank.";

		public static final String PHONE_IS_NULL = "Phone cannot be null.";
		public static final String PHONE_IS_BLANK = "Phone cannot be blank.";

		public static final String VALUE_IS_NULL = "Cannot instantiate with null value.";
		public static final String VALUE_IS_NEGATIVE = "Cannot instantiate with negative value.";
		public static final String VALUE_IS_NEGATIVE_OR_ZERO = "Cannot instantiate with negative (or zero) value.";
	}

	public static final String CUSTOMER_ARCHIVED = "This customer is already archived.";
}
