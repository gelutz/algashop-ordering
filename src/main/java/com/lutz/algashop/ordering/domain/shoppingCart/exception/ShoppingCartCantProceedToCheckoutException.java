package com.lutz.algashop.ordering.domain.shoppingCart.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {
	public ShoppingCartCantProceedToCheckoutException(ShoppingCartId id, String reason) {
		super(String.format("ShoppingCart [%s]: Cannot proceed to checkout. %s", id, reason));
	}
}
