package com.lutz.algashop.ordering.domain.commons;

import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItemId;

public class ErrorMessages {
	public static final String CUSTOMER_ARCHIVED = "This customer is already archived.";
	public static final String CUSTOMER_NOT_FOUND = "Customer not found.";

	public static class Fields {
		public static String fieldIsNullMessage(String field) {
			return field + " is null.";
		}

		public static final String EMAIL_IS_NULL = fieldIsNullMessage("Email");
		public static final String EMAIL_IS_BLANK = "Email is blank.";
		public static final String EMAIL_IS_INVALID = "Email is invalid.";

		public static final String FULL_NAME_IS_NULL = fieldIsNullMessage("FullName");
		public static final String FULL_NAME_IS_BLANK = "FullName cannot be blank.";

		public static final String BIRTHDATE_IS_NULL = fieldIsNullMessage("Birthdate");
		public static final String BIRTHDATE_IS_IN_FUTURE = "Birthdate must be a past date.";

		public static final String DOCUMENT_IS_NULL = fieldIsNullMessage("Document");
		public static final String DOCUMENT_IS_BLANK = "Document cannot be blank.";

		public static final String PHONE_IS_NULL = fieldIsNullMessage("Phone");
		public static final String PHONE_IS_BLANK = "Phone cannot be blank.";

		public static final String VALUE_IS_NULL = "Cannot instantiate with null value.";
		public static final String VALUE_IS_NEGATIVE = "Cannot instantiate with negative value.";
		public static final String VALUE_IS_NEGATIVE_OR_ZERO = "Cannot instantiate with negative (or zero) value.";
	}




}
