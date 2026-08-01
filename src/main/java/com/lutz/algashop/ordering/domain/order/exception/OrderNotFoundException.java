package com.lutz.algashop.ordering.domain.order.exception;

import com.lutz.algashop.ordering.domain.DomainEntityNotFoundException;

public class OrderNotFoundException extends DomainEntityNotFoundException {
	public OrderNotFoundException() {
		super(ErrorMessages.ORDER_NOT_FOUND);
	}
}
