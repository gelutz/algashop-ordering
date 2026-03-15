package com.lutz.algashop.ordering.infrastructure.persistence.order;

import com.lutz.algashop.ordering.domain.order.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemPersistenceEntityAssembler {

	OrderItemPersistenceEntity fromDomain(OrderItem item) {
		return merge(new OrderItemPersistenceEntity(), item);
	}

	OrderItemPersistenceEntity merge(OrderItemPersistenceEntity orderPersistenceEntity, OrderItem item) {
		orderPersistenceEntity.setId(item.id().value().toLong());
		orderPersistenceEntity.setProductId(item.productId().value());
		orderPersistenceEntity.setProductName(item.productName().toString());
		orderPersistenceEntity.setPrice(item.price().value());
		orderPersistenceEntity.setQuantity(item.quantity().value());
		orderPersistenceEntity.setTotalAmount(item.totalAmount().value());

		return orderPersistenceEntity;
	}
}
