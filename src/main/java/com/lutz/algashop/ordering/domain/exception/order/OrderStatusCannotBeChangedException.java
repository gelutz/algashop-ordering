package com.lutz.algashop.ordering.domain.exception.order;

import com.lutz.algashop.ordering.domain.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.exception.DomainException;
import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import com.lutz.algashop.ordering.domain.vo.OrderId;

public class OrderStatusCannotBeChangedException extends DomainException {
	private final OrderId orderId;
	private final OrderStatus oldStatus;
	private final OrderStatus newStatus;

	public OrderStatusCannotBeChangedException(OrderId orderId, OrderStatus oldStatus, OrderStatus newStatus) {
		super(ErrorMessages.Orders.orderStatusCannotBeChanged(orderId, oldStatus, newStatus));
		this.orderId = orderId;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
	}
}
