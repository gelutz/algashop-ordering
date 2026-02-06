package com.lutz.algashop.ordering.domain.exception;

import com.lutz.algashop.ordering.domain.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.vo.OrderId;
import com.lutz.algashop.ordering.domain.vo.OrderItemId;

import java.time.LocalDate;

public class ErrorMessages {
	public static class Fields {
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

	public static class Orders {
		public static String orderCannotBePlaced(OrderId orderId, String reason) {
			return orderMessageWrapper(orderId, String.format("The order could not be placed. %s", reason));
		}

		public static String orderExpectedDeliveryDateIsInvalid(OrderId orderId, LocalDate expectedDeliveryDate) {
			return orderMessageWrapper(orderId, String.format("The delivery date cannot be set to a past date (%s).", expectedDeliveryDate));
		}
		public static String orderStatusCannotBeChanged(OrderId orderId, OrderStatus oldStatus, OrderStatus newStatus) {
			return orderMessageWrapper(orderId, String.format(
					"Order status [%s] cannot be changed to [%s].",oldStatus, newStatus
			                                                 ));
		}

		private static String orderMessageWrapper(OrderId orderId, String message) {
			return String.format("Order [%s]: %s", orderId, message);
		}

		public static String orderDoesNotContainOrderItem(OrderId orderId, OrderItemId orderItemId) {
			return orderMessageWrapper(orderId, String.format("The order does not contain this item: [%s]", orderItemId));
		}
	}

	public static final String CUSTOMER_ARCHIVED = "This customer is already archived.";

}
