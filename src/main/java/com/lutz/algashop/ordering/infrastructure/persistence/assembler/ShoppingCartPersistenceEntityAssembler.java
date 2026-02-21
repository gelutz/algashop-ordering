package com.lutz.algashop.ordering.infrastructure.persistence.assembler;

import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCart;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartItem;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceEntityAssembler {
	private final ShoppingCartItemPersistenceEntityAssembler itemAssembler;
	private final CustomerPersistenceEntityRepository customerRepository;

	public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
		return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
	}

	public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity persistenceEntity, ShoppingCart shoppingCart) {
		persistenceEntity.setId(shoppingCart.id().value());
		persistenceEntity.setTotalAmount(shoppingCart.totalAmount().value());
		persistenceEntity.setTotalItems(shoppingCart.totalItems().value());
		persistenceEntity.setCreatedAt(shoppingCart.createdAt());
		persistenceEntity.setVersion(shoppingCart.version());

		CustomerPersistenceEntity customerPersistenceReference = customerRepository
				.getReferenceById(shoppingCart.customerId().value());
		persistenceEntity.setCustomer(customerPersistenceReference);

		Set<ShoppingCartItemPersistenceEntity> mergedItems = mergeItems(shoppingCart, persistenceEntity);
		persistenceEntity.setItems(mergedItems);
		return persistenceEntity;
	}

	private Set<ShoppingCartItemPersistenceEntity> mergeItems(ShoppingCart shoppingCart, ShoppingCartPersistenceEntity shoppingCartPersistenceEntity) {
		Set<ShoppingCartItem> newOrUpdateItems = shoppingCart.items();
		if (newOrUpdateItems == null || newOrUpdateItems.isEmpty()) {
			return new HashSet<>();
		}

		Set<ShoppingCartItemPersistenceEntity> existingItems = shoppingCartPersistenceEntity.getItems();

		if (existingItems == null || existingItems.isEmpty()) {
			return newOrUpdateItems.stream()
					.map(itemAssembler::fromDomain)
					.peek(item -> item.setShoppingCart(shoppingCartPersistenceEntity))
					.collect(Collectors.toSet());
		}

		Map<Object, ShoppingCartItemPersistenceEntity> existingMap = existingItems.stream()
				.collect(Collectors.toMap(
						ShoppingCartItemPersistenceEntity::getId,
						item -> item
				));

		return newOrUpdateItems
				.stream()
				.map(shoppingCartItem -> {
					ShoppingCartItemPersistenceEntity itemPersistence = existingMap.getOrDefault(
							shoppingCartItem.id().value(),
							new ShoppingCartItemPersistenceEntity()
					);
					return itemAssembler.merge(itemPersistence, shoppingCartItem);
				})
				.collect(Collectors.toSet());
	}
}
