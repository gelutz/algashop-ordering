package com.lutz.algashop.ordering.infrastructure.persistence.assembler;

import com.lutz.algashop.ordering.domain.entity.customer.vo.Address;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.OrderItem;
import com.lutz.algashop.ordering.domain.entity.order.vo.Billing;
import com.lutz.algashop.ordering.domain.entity.order.vo.Recipient;
import com.lutz.algashop.ordering.domain.entity.order.vo.Shipping;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.AddressEmbeddable;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.BillingEmbeddable;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.RecipientEmbeddable;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.ShippingEmbeddable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderPersistenceEntityAssembler {
	private final OrderItemPersistenceEntityAssembler itemAssembler;

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
		orderPersistenceEntity.setBilling(billingFromDomain(order.billing()));
		orderPersistenceEntity.setShipping(shippingFromDomain(order.shipping()));

		Set<OrderItemPersistenceEntity> mergedItems = mergeItems(orderPersistenceEntity, order);
		orderPersistenceEntity.setItems(mergedItems);
		return orderPersistenceEntity;
	}

	private Set<OrderItemPersistenceEntity> mergeItems(OrderPersistenceEntity orderPersistenceEntity, Order order) {
		Set<OrderItem> newItems = order.items();

		if (newItems == null || newItems.isEmpty()) return new HashSet<>();

		Set<OrderItemPersistenceEntity> existingItems = orderPersistenceEntity.getItems();
		if (existingItems == null || existingItems.isEmpty()) {
			return newItems.stream()
			               .map(itemAssembler::fromDomain)
			               .peek(item -> item.setOrder(orderPersistenceEntity))
			               .collect(Collectors.toSet());
		}

		// creates a map with id -> item that will be updated with newItems
		Map<Long, OrderItemPersistenceEntity> mappedItems = existingItems
				.stream()
				.collect(Collectors.toMap(
						OrderItemPersistenceEntity::getId,
						item -> item)
		        );

		return newItems
				.stream()
				.map(item -> {
					OrderItemPersistenceEntity currentItem = mappedItems.getOrDefault(
							item.id().value().toLong(),
							new OrderItemPersistenceEntity()
					                                                               );
					return itemAssembler.merge(currentItem, item);
				}).collect(Collectors.toSet());
	}

	private BillingEmbeddable billingFromDomain(Billing entity) {
		if (entity == null) return null;
		Objects.requireNonNull(entity.fullName());
		Objects.requireNonNull(entity.document());
		Objects.requireNonNull(entity.phone());
		Objects.requireNonNull(entity.email());
		Objects.requireNonNull(entity.address());
		return BillingEmbeddable.builder()
								.firstName(entity.fullName().firstName())
				                .lastName(entity.fullName().lastName())
				                .document(entity.document().toString())
				                .phone(entity.phone().toString())
				                .email(entity.email().toString())
				                .address(addressFromDomain(entity.address()))
				                .build();
	}

	private ShippingEmbeddable shippingFromDomain(Shipping entity) {
		if (entity == null) return null;
		Objects.requireNonNull(entity.cost());
		Objects.requireNonNull(entity.expectedDeliveryDate());
		Objects.requireNonNull(entity.recipient());
		Objects.requireNonNull(entity.address());
		return ShippingEmbeddable.builder()
		                         .cost(entity.cost().value())
		                         .expectedDeliveryDate(entity.expectedDeliveryDate())
		                         .recipient(recipientFromDomain(entity.recipient()))
		                         .address(addressFromDomain(entity.address()))
		                         .build();
	}

	private AddressEmbeddable addressFromDomain(Address entity) {
		if (entity == null) return null;
		Objects.requireNonNull(entity.street());
		Objects.requireNonNull(entity.number());
		Objects.requireNonNull(entity.city());
		Objects.requireNonNull(entity.state());
		Objects.requireNonNull(entity.zipCode());
		return AddressEmbeddable.builder()
		                        .street(entity.street())
		                        .number(entity.number())
		                        .complement(entity.complement())
		                        .neighborhood(entity.neighborhood())
		                        .city(entity.city())
		                        .state(entity.state())
		                        .zipCode(entity.zipCode().toString())
		                        .build();
	}

	private RecipientEmbeddable recipientFromDomain(Recipient entity) {
		if (entity == null) return null;
		Objects.requireNonNull(entity.fullName());
		Objects.requireNonNull(entity.document());
		Objects.requireNonNull(entity.phone());
		return RecipientEmbeddable.builder()
		                          .firstName(entity.fullName().firstName())
		                          .lastName(entity.fullName().lastName())
		                          .document(entity.document().toString())
		                          .phone(entity.phone().toString())
		                          .build();
	}
}
