package com.lutz.algashop.ordering.domain.shoppingCart.entity;

import com.lutz.algashop.ordering.domain.IdGenerator;

import java.util.UUID;

public record ShoppingCartId(UUID value) {
	public ShoppingCartId() {
		this(IdGenerator.generateTimeBasedUUID());
	}
}
