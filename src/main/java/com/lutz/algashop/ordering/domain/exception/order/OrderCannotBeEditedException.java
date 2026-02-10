package com.lutz.algashop.ordering.domain.exception.order;

import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.exception.DomainException;
import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;

public class OrderCannotBeEditedException extends DomainException {
	public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
		super(ErrorMessages.Orders.orderCannotBeEdited(id, status));
	}
}
