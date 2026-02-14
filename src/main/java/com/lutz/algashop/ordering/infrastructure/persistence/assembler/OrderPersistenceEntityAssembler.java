package com.lutz.algashop.ordering.infrastructure.persistence.assembler;

import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceEntityAssembler {
	public OrderPersistenceEntity fromDomain(Order order) {
		return merge(new OrderPersistenceEntity(), order);
	}

	public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
		orderPersistenceEntity.setId(order.id().value().toLong());
		orderPersistenceEntity.setCustomerId(order.customerId().value());
		orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
		orderPersistenceEntity.setTotalItems(order.itemsAmount().value());
		orderPersistenceEntity.setStatus(order.status().name());
		orderPersistenceEntity.setPaymentMethod(order.paymentMethod().name());
		orderPersistenceEntity.setPlacedAt(order.placedAt());
		orderPersistenceEntity.setPaidAt(order.paidAt());
		orderPersistenceEntity.setReadyAt(order.readyAt());
		orderPersistenceEntity.setCanceledAt(order.canceledAt());
		orderPersistenceEntity.setVersion(order.version());

		return orderPersistenceEntity;
	}
}
