package com.lutz.algashop.ordering.domain.shoppingCart.exception;

import com.lutz.algashop.ordering.domain.DomainEntityNotFoundException;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;

public class ShoppingCartNotFoundException extends DomainEntityNotFoundException {

	public ShoppingCartNotFoundException() {
		super("Shopping cart not found");
	}

	public ShoppingCartNotFoundException(ShoppingCartId id) {
		super(ErrorMessages.shoppingCartNotFound(id));
	}
}
