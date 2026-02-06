package com.lutz.algashop.ordering.domain.exception;

import com.lutz.algashop.ordering.domain.vo.OrderId;

import java.time.LocalDate;

public class InvalidShippingDeliveryDateException extends DomainException {
	public InvalidShippingDeliveryDateException(OrderId orderId, LocalDate expectedDeliveryDate) {
		super(ErrorMessages.Orders.orderExpectedDeliveryDateIsInvalid(orderId, expectedDeliveryDate));
	}
}
