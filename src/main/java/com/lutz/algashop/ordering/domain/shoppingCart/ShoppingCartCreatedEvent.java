package com.lutz.algashop.ordering.domain.shoppingCart;

import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;

import java.time.OffsetDateTime;

public record ShoppingCartCreatedEvent(
		ShoppingCartId shoppingCartId,
		CustomerId customerId,
		OffsetDateTime createdAt
) {
}
