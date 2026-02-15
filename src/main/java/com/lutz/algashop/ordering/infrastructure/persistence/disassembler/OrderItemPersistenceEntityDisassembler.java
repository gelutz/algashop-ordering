package com.lutz.algashop.ordering.infrastructure.persistence.disassembler;

import com.lutz.algashop.ordering.domain.entity.order.OrderItem;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderItemPersistenceEntityDisassembler {
	Set<OrderItem> fromPersistence(Set<OrderItemPersistenceEntity> itemsPersistence) {
		if (itemsPersistence == null || itemsPersistence.isEmpty()) return new HashSet<>();

		return itemsPersistence
				.stream()
				.map(ip -> OrderItem.existing()
				                    .id(new OrderItemId(ip.getId()))
				                    .orderId(new OrderId(ip.getOrderId()))
				                    .productId(new ProductId(ip.getProductId()))
				                    .productName(new ProductName(ip.getProductName()))
				                    .price(new Money(ip.getPrice()))
				                    .quantity(new Quantity(ip.getQuantity()))
				                    .totalAmount(new Money(ip.getTotalAmount()))
				                    .build())
				.collect(Collectors.toSet());
	}
}
