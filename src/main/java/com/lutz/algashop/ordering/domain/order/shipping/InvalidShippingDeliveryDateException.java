package com.lutz.algashop.ordering.domain.order.shipping;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.exception.ErrorMessages;

import java.time.LocalDate;

public class InvalidShippingDeliveryDateException extends DomainException {
	public InvalidShippingDeliveryDateException(OrderId orderId, LocalDate expectedDeliveryDate) {
		super(ErrorMessages.orderExpectedDeliveryDateIsInvalid(orderId, expectedDeliveryDate));
	}
}
