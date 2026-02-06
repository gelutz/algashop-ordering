package com.lutz.algashop.ordering.domain.exception;

import com.lutz.algashop.ordering.domain.vo.OrderId;

public class OrderCannotBePlacedException extends DomainException {
	public OrderCannotBePlacedException(OrderId orderId) {
		super(ErrorMessages.Orders.orderCannotBePlacedException(orderId));
	}
}
