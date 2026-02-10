package com.lutz.algashop.ordering.domain.entity.shoppingCart;

import com.lutz.algashop.ordering.domain.utils.IdGenerator;

import java.util.UUID;

public record ShoppingCartItemId(UUID value) {
	public ShoppingCartItemId() {
		this(IdGenerator.generateTimeBasedUUID());
	}
}
