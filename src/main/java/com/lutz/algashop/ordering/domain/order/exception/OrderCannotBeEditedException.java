package com.lutz.algashop.ordering.domain.order.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.order.OrderId;

public class OrderCannotBeEditedException extends DomainException {
	public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
		super(ErrorMessages.orderCannotBeEdited(id, status));
	}
}
