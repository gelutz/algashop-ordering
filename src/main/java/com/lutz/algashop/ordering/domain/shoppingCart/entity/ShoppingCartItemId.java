package com.lutz.algashop.ordering.domain.shoppingCart.entity;

import com.lutz.algashop.ordering.domain.IdGenerator;

import java.util.UUID;

public record ShoppingCartItemId(UUID value) {
	public ShoppingCartItemId() {
		this(IdGenerator.generateTimeBasedUUID());
	}
}
