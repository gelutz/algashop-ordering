package com.lutz.algashop.ordering.domain.exception.order;

import com.lutz.algashop.ordering.domain.exception.DomainException;
import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import com.lutz.algashop.ordering.domain.vo.OrderId;
import com.lutz.algashop.ordering.domain.vo.OrderItemId;

public class OrderDoesNotContainOrderItemException extends DomainException {
	public OrderDoesNotContainOrderItemException(OrderId id, OrderItemId orderItemId) {
		super(ErrorMessages.Orders.orderDoesNotContainOrderItem(id, orderItemId));
	}
}
