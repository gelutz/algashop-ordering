package com.lutz.algashop.ordering.application.shoppingcart.management.builder;

import com.lutz.algashop.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import com.lutz.algashop.ordering.domain.product.builder.ProductTestBuilder;

import java.util.UUID;

public class ShoppingCartItemInputTestBuilder {

	private ShoppingCartItemInputTestBuilder() {
	}

	public static ShoppingCartItemInput.ShoppingCartItemInputBuilder aShoppingCartItemInput() {
		return ShoppingCartItemInput.builder()
				.quantity(1)
				.productId(ProductTestBuilder.DEFAULT_PRODUCT_ID.value())
				.shoppingCartId(UUID.randomUUID());
	}
}
