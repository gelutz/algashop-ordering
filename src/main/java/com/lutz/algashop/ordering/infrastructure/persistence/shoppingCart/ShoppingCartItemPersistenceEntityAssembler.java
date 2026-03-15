package com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart;

import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItem;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartItemPersistenceEntityAssembler {

	ShoppingCartItemPersistenceEntity fromDomain(ShoppingCartItem item) {
		return merge(new ShoppingCartItemPersistenceEntity(), item);
	}

	ShoppingCartItemPersistenceEntity merge(ShoppingCartItemPersistenceEntity persistenceEntity, ShoppingCartItem item) {
		persistenceEntity.setId(item.id().value());
		persistenceEntity.setProductId(item.productId().value());
		persistenceEntity.setProductName(item.name().toString());
		persistenceEntity.setPrice(item.price().value());
		persistenceEntity.setQuantity(item.quantity().value());
		persistenceEntity.setTotalAmount(item.totalAmount().value());
		persistenceEntity.setAvailable(item.available());

		return persistenceEntity;
	}
}
