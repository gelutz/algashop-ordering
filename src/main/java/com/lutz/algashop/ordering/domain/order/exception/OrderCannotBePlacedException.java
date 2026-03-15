package com.lutz.algashop.ordering.domain.order.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.order.OrderId;

public class OrderCannotBePlacedException extends DomainException {
	public OrderCannotBePlacedException(OrderId orderId) {
		super(ErrorMessages.orderCannotBePlaced(orderId, "The order could not be placed."));
	}

	private OrderCannotBePlacedException(OrderId orderId, String message) {
		super(message);
	}

	public static OrderCannotBePlacedException shippingInfoIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.orderCannotBePlaced(orderId, "Field shippingInfo cannot be null."));
	}

	public static OrderCannotBePlacedException billingInfoIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.orderCannotBePlaced(orderId, "Field billingInfo cannot be null."));
	}

	public static OrderCannotBePlacedException paymentMethodIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.orderCannotBePlaced(orderId, "Field paymentMethod cannot be null."));
	}

	public static OrderCannotBePlacedException itemsIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.orderCannotBePlaced(orderId, "Field items cannot be null."));
	}

	public static OrderCannotBePlacedException emptyItems(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.orderCannotBePlaced(orderId, "Field items cannot be empty."));
	}
}
