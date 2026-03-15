package com.lutz.algashop.ordering.domain.order.exception;

import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.OrderItemId;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;

import java.time.LocalDate;

public class ErrorMessages {
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

	public static String orderHasWrongStatus(OrderId orderId, OrderStatus is, OrderStatus shouldBe) {
		return orderMessageWrapper(orderId, String.format("This order has the wrong status. Is [%s] but should be [%s]", is, shouldBe));
	}

	public static String orderDoesNotContainOrderItem(OrderId orderId, OrderItemId orderItemId) {
		return orderMessageWrapper(orderId, String.format("The order does not contain this item: [%s]", orderItemId));
	}

	public static String orderNotFromThisCustomer(OrderId orderId, CustomerId customerId) {
		return orderMessageWrapper(orderId, String.format("This order does not belong to the customer %s", customerId));
	}

	private static String orderMessageWrapper(OrderId orderId, String message) {
		return String.format("Order [%s]: %s", orderId, message);
	}
}
