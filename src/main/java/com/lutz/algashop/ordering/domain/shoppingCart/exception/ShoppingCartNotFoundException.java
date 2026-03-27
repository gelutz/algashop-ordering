package com.lutz.algashop.ordering.domain.shoppingCart.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;

public class ShoppingCartNotFoundException extends DomainException {
	public ShoppingCartNotFoundException(ShoppingCartId id) {
		super(ErrorMessages.shoppingCartNotFound(id));
	}
}
