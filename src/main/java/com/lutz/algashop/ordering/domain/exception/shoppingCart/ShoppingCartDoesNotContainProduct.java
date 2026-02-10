package com.lutz.algashop.ordering.domain.exception.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartId;
import com.lutz.algashop.ordering.domain.exception.DomainException;
import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;

public class ShoppingCartDoesNotContainProduct extends DomainException {
	public ShoppingCartDoesNotContainProduct(ShoppingCartId id, ProductId productId) {
		super(ErrorMessages.ShoppingCart.cartDoesNotContainProduct(id, productId));
	}
}

