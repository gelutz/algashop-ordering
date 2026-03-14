package com.lutz.algashop.ordering.domain.exception.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartId;
import com.lutz.algashop.ordering.domain.exception.DomainException;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {
	public ShoppingCartCantProceedToCheckoutException(ShoppingCartId id, String reason) {
		super(String.format("ShoppingCart [%s]: Cannot proceed to checkout. %s", id, reason));
	}
}
