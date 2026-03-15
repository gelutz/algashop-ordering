package com.lutz.algashop.ordering.domain.shoppingCart.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;

public class ShoppingCartDoesNotContainProduct extends DomainException {
	public ShoppingCartDoesNotContainProduct(ShoppingCartId id, ProductId productId) {
		super(ErrorMessages.cartDoesNotContainProduct(id, productId));
	}
}

