package com.lutz.algashop.ordering.domain.order.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.order.OrderId;

public class OrderNotFromThisCustomerException extends DomainException {
	public OrderNotFromThisCustomerException(OrderId orderId, CustomerId customerId) {
		super(ErrorMessages.orderNotFromThisCustomer(orderId, customerId));
	}
}
