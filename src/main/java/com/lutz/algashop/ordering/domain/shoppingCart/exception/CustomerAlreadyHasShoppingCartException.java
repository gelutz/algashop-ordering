package com.lutz.algashop.ordering.domain.shoppingCart.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.customer.CustomerId;

public class CustomerAlreadyHasShoppingCartException extends DomainException {
	public CustomerAlreadyHasShoppingCartException(CustomerId customerId) {
		super(ErrorMessages.customerAlreadyHasShoppingCart(customerId));
	}
}
