package com.lutz.algashop.ordering.domain.exception;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;

public class OrderNotFromThisCustomerException extends DomainException {
	public OrderNotFromThisCustomerException(OrderId orderId, CustomerId customerId) {
		super(ErrorMessages.Orders.orderNotFromThisCustomer(orderId, customerId));
	}
}
