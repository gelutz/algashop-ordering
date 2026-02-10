package com.lutz.algashop.ordering.domain.exception;

import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;

import java.time.LocalDate;

public class InvalidShippingDeliveryDateException extends DomainException {
	public InvalidShippingDeliveryDateException(OrderId orderId, LocalDate expectedDeliveryDate) {
		super(ErrorMessages.Orders.orderExpectedDeliveryDateIsInvalid(orderId, expectedDeliveryDate));
	}
}
