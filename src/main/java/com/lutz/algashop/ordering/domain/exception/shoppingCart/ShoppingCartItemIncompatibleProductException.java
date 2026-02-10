package com.lutz.algashop.ordering.domain.exception.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartItemId;
import com.lutz.algashop.ordering.domain.exception.DomainException;
import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;
import lombok.NonNull;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
	public ShoppingCartItemIncompatibleProductException(@NonNull ShoppingCartItemId shoppingCartItemId, @NonNull ProductId that, @NonNull ProductId other) {
		super(ErrorMessages.ShoppingCartItem.shoppingCartProductsDontMatch(shoppingCartItemId, that, other));
	}
}
