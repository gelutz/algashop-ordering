package com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.product.ProductName;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItem;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItemId;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartItemPersistenceEntityDisassembler {

	Set<ShoppingCartItem> fromPersistence(Set<ShoppingCartItemPersistenceEntity> itemsPersistence) {
		if (itemsPersistence == null || itemsPersistence.isEmpty()) return new HashSet<>();

		return itemsPersistence
				.stream()
				.map(this::fromPersistenceSingle)
				.collect(Collectors.toSet());
	}

	ShoppingCartItem fromPersistenceSingle(ShoppingCartItemPersistenceEntity item) {
		return ShoppingCartItem.existing()
				.id(new ShoppingCartItemId(item.getId()))
				.shoppingCartId(new ShoppingCartId(item.getShoppingCartId()))
				.productId(new ProductId(item.getProductId()))
				.name(new ProductName(item.getProductName()))
				.price(new Money(item.getPrice()))
				.quantity(new Quantity(item.getQuantity()))
				.totalAmount(new Money(item.getTotalAmount()))
				.available(item.getAvailable())
				.build();
	}
}
