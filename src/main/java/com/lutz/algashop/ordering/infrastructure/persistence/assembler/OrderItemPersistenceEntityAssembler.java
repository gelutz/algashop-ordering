package com.lutz.algashop.ordering.infrastructure.persistence.assembler;

import com.lutz.algashop.ordering.domain.entity.order.OrderItem;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
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
