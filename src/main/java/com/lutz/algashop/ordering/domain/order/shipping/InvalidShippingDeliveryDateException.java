package com.lutz.algashop.ordering.domain.order.shipping;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.commons.ErrorMessages;
import com.lutz.algashop.ordering.domain.order.vo.OrderId;

import java.time.LocalDate;

public class InvalidShippingDeliveryDateException extends DomainException {
	public InvalidShippingDeliveryDateException(OrderId orderId, LocalDate expectedDeliveryDate) {
		super(ErrorMessages.Orders.orderExpectedDeliveryDateIsInvalid(orderId, expectedDeliveryDate));
	}
}
