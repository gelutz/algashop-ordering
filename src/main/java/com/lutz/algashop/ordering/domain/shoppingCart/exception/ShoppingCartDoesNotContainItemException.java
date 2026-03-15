package com.lutz.algashop.ordering.domain.shoppingCart.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.commons.ErrorMessages;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItemId;

public class ShoppingCartDoesNotContainItemException extends DomainException {
	public ShoppingCartDoesNotContainItemException(ShoppingCartId id, ShoppingCartItemId itemId) {
		super(ErrorMessages.ShoppingCart.cartDoesNotContainCartItem(id, itemId));
	}
}
