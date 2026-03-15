package com.lutz.algashop.ordering.domain.order.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.commons.ErrorMessages;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.OrderItemId;

public class OrderDoesNotContainOrderItemException extends DomainException {
	public OrderDoesNotContainOrderItemException(OrderId id, OrderItemId orderItemId) {
		super(ErrorMessages.Orders.orderDoesNotContainOrderItem(id, orderItemId));
	}
}
