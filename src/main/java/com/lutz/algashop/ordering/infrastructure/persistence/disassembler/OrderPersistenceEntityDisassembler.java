package com.lutz.algashop.ordering.infrastructure.persistence.disassembler;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class OrderPersistenceEntityDisassembler {
	public Order fromPersistence(OrderPersistenceEntity order) {
		return Order.existing()
				.id(new OrderId(order.getId()))
				    .customerId(new CustomerId(order.getCustomerId()))
				    .totalAmount(new Money(order.getTotalAmount()))
				    .itemsAmount(new Quantity(order.getTotalItems()))
				    .status(OrderStatus.valueOf(order.getStatus()))
				    .paymentMethod(PaymentMethod.valueOf(order.getPaymentMethod()))
				    .placedAt(order.getPlacedAt())
				    .paidAt(order.getPaidAt())
				    .readyAt(order.getReadyAt())
				    .canceledAt(order.getCanceledAt())
				    .items(new HashSet<>())
					.version(order.getVersion())
				.build();
	}
}
