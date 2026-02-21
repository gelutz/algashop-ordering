package com.lutz.algashop.ordering.infrastructure.persistence.disassembler;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCart;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartId;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
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
