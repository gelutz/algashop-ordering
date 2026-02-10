package com.lutz.algashop.ordering.domain.exception.messages;

import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderItemId;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartId;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartItemId;

import java.time.LocalDate;

public class ErrorMessages {
	public static final String CUSTOMER_ARCHIVED = "This customer is already archived.";

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

	public static class Orders {
		public static String orderCannotBePlaced(OrderId orderId, String reason) {
			return orderMessageWrapper(orderId, String.format("The order could not be placed. %s", reason));
		}

		public static String orderCannotBeEdited(OrderId orderId, OrderStatus status) {
			return orderMessageWrapper(orderId, String.format("This order with status %s cannot be edited.", status.name()));
		}

		public static String orderExpectedDeliveryDateIsInvalid(OrderId orderId, LocalDate expectedDeliveryDate) {
			return orderMessageWrapper(orderId, String.format("The delivery date cannot be set to a past date (%s).", expectedDeliveryDate));
		}
		public static String orderStatusCannotBeChanged(OrderId orderId, OrderStatus oldStatus, OrderStatus newStatus) {
			return orderMessageWrapper(orderId, String.format(
					"Order status [%s] cannot be changed to [%s].",oldStatus, newStatus
			                                                 ));
		}

		public static String orderDoesNotContainOrderItem(OrderId orderId, OrderItemId orderItemId) {
			return orderMessageWrapper(orderId, String.format("The order does not contain this item: [%s]", orderItemId));
		}

		private static String orderMessageWrapper(OrderId orderId, String message) {
			return String.format("Order [%s]: %s", orderId, message);
		}
	}

	public static class Products {
		public static String productOutOfStock(ProductId productId) {
			return productMessageWrapper(productId, "The product is out of stock.");
		}

		private static String productMessageWrapper(ProductId productId, String message) {
			return String.format("Product [%s]: %s", productId, message);
		}
	}

	public class ShoppingCart {
		public static String cartDoesNotContainCartItem(ShoppingCartId id, ShoppingCartItemId itemid) {
			return shoppingCartMessageWrapper(id, String.format("The shopping cart does not contain this item: [%s]", itemid));
		}

		public static String cartDoesNotContainProduct(ShoppingCartId id, ProductId productId) {
			return shoppingCartMessageWrapper(id, String.format("The shopping cart does not contain this product: [%s]", productId));
		}

		private static String shoppingCartMessageWrapper(ShoppingCartId id, String message) {
			return String.format("ShoppingCart [%s]: %s", id, message);
		}
	}

	public class ShoppingCartItem {
		public static String shoppingCartProductsDontMatch(ShoppingCartItemId id, ProductId that, ProductId other) {
			return shoppingCartItemMessageWrapper(
					id,
					String.format("Cannot refresh. Given Product id %s does not match Shopping cart item id %s", that, other)
			                                     );

		}


		private static String shoppingCartItemMessageWrapper(ShoppingCartItemId id, String message) {
			return String.format("ShoppingCartItem [%s]: %s", id, message);
		}
	}
}
