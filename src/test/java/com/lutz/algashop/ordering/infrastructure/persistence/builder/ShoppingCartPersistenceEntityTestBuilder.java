package com.lutz.algashop.ordering.infrastructure.persistence.builder;

import com.lutz.algashop.ordering.domain.IdGenerator;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public class ShoppingCartPersistenceEntityTestBuilder {

	public static ShoppingCartPersistenceEntity.ShoppingCartPersistenceEntityBuilder existing() {
		return ShoppingCartPersistenceEntity.builder()
				.id(IdGenerator.generateTimeBasedUUID())
				.customer(CustomerPersistenceEntityTestBuilder.existing().build())
				.totalItems(2)
				.totalAmount(new BigDecimal("100"))
				.createdAt(OffsetDateTime.now())
				.items(Set.of(anItem().build()));
	}

	public static ShoppingCartItemPersistenceEntity.ShoppingCartItemPersistenceEntityBuilder anItem() {
		return ShoppingCartItemPersistenceEntity.builder()
				.id(IdGenerator.generateTimeBasedUUID())
				.productId(IdGenerator.generateTimeBasedUUID())
				.productName("Random product")
				.price(new BigDecimal("10"))
				.quantity(1)
				.totalAmount(new BigDecimal("10"))
				.available(true);
	}
}
