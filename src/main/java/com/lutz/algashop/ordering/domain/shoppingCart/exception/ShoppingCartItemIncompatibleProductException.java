package com.lutz.algashop.ordering.domain.shoppingCart.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.commons.ErrorMessages;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItemId;
import lombok.NonNull;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
	public ShoppingCartItemIncompatibleProductException(@NonNull ShoppingCartItemId shoppingCartItemId, @NonNull ProductId that, @NonNull ProductId other) {
		super(ErrorMessages.ShoppingCartItem.shoppingCartProductsDontMatch(shoppingCartItemId, that, other));
	}
}
