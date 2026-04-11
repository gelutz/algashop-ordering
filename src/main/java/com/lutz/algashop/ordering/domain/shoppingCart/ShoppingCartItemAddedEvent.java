package com.lutz.algashop.ordering.domain.shoppingCart;

import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;

import java.time.OffsetDateTime;

public record ShoppingCartItemAddedEvent(
		ShoppingCartId shoppingCartId,
		CustomerId customerId,
		ProductId productId,
		OffsetDateTime addedAt
) {
}
