package com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceEntityDisassembler {
	private final ShoppingCartItemPersistenceEntityDisassembler itemDisassembler;

	public ShoppingCart fromPersistence(ShoppingCartPersistenceEntity entity) {
		return ShoppingCart.existing()
				.id(new ShoppingCartId(entity.getId()))
				.customerId(new CustomerId(entity.getCustomerId()))
				.totalAmount(new Money(entity.getTotalAmount()))
				.totalItems(new Quantity(entity.getTotalItems()))
				.createdAt(entity.getCreatedAt())
				.items(itemDisassembler.fromPersistence(entity.getItems()))
				.version(entity.getVersion())
				.build();
	}
}
