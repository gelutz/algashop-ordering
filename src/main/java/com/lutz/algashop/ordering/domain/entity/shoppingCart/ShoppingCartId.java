package com.lutz.algashop.ordering.domain.entity.shoppingCart;

import com.lutz.algashop.ordering.domain.utils.IdGenerator;

import java.util.UUID;

public record ShoppingCartId(UUID value) {
	public ShoppingCartId() {
		this(IdGenerator.generateTimeBasedUUID());
	}
}
