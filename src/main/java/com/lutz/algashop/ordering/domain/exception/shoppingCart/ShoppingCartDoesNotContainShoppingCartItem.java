package com.lutz.algashop.ordering.domain.exception.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartId;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartItemId;
import com.lutz.algashop.ordering.domain.exception.DomainException;
import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;

public class ShoppingCartDoesNotContainShoppingCartItem extends DomainException {
	public ShoppingCartDoesNotContainShoppingCartItem(ShoppingCartId id, ShoppingCartItemId itemId) {
		super(ErrorMessages.ShoppingCart.cartDoesNotContainCartItem(id, itemId));
	}
}
