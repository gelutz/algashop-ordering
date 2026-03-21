package com.lutz.algashop.ordering.domain.order.exception;

import com.lutz.algashop.ordering.domain.DomainException;

public class OrderNotFoundException extends DomainException {
	public OrderNotFoundException() {
		super(ErrorMessages.ORDER_NOT_FOUND);
	}
}
