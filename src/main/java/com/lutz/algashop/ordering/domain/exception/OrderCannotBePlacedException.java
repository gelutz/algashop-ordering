package com.lutz.algashop.ordering.domain.exception;

import com.lutz.algashop.ordering.domain.vo.OrderId;

public class OrderCannotBePlacedException extends DomainException {
	public OrderCannotBePlacedException(OrderId orderId) {
		super(ErrorMessages.Orders.orderCannotBePlacedException(orderId, "The order could not be placed."));
	}

	private OrderCannotBePlacedException(OrderId orderId, String message) {
		super(message);
	}

	public static OrderCannotBePlacedException shippingInfoIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.Orders.orderCannotBePlacedException(orderId, "Field shippingInfo cannot be null."));
	}

	public static OrderCannotBePlacedException shippingCostIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.Orders.orderCannotBePlacedException(orderId, "Field shippingCost cannot be null."));
	}

	public static OrderCannotBePlacedException billingInfoIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.Orders.orderCannotBePlacedException(orderId, "Field billingInfo cannot be null."));
	}

	public static OrderCannotBePlacedException paymentMethodIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.Orders.orderCannotBePlacedException(orderId, "Field paymentMethod cannot be null."));
	}

	public static OrderCannotBePlacedException expectedDeliveryDateIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.Orders.orderCannotBePlacedException(orderId, "Field expectedDeliveryDate cannot be null."));
	}

	public static OrderCannotBePlacedException itemsIsNull(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.Orders.orderCannotBePlacedException(orderId, "Field items cannot be null."));
	}

	public static OrderCannotBePlacedException emptyItems(OrderId orderId) {
		return new OrderCannotBePlacedException(orderId,
				ErrorMessages.Orders.orderCannotBePlacedException(orderId, "Field items cannot be empty."));
	}
}
