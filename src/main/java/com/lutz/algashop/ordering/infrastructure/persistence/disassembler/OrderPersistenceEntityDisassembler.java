package com.lutz.algashop.ordering.infrastructure.persistence.disassembler;

import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.AddressEmbeddable;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.BillingEmbeddable;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.RecipientEmbeddable;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.ShippingEmbeddable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderPersistenceEntityDisassembler {
	private final OrderItemPersistenceEntityDisassembler itemDisassembler;

	public Order fromPersistence(OrderPersistenceEntity order) {
		return Order.existing()
				.id(new OrderId(order.getId()))
			    .customerId(new CustomerId(order.getCustomerId()))
			    .totalAmount(new Money(order.getTotalAmount()))
			    .itemsAmount(new Quantity(order.getTotalItems()))
			    .status(OrderStatus.valueOf(order.getStatus()))
			    .paymentMethod(PaymentMethod.valueOf(order.getPaymentMethod()))
				.billing(billingFromPersistence(order.getBilling()))
				.shipping(shippingFromPersistence(order.getShipping()))
			    .placedAt(order.getPlacedAt())
			    .paidAt(order.getPaidAt())
			    .readyAt(order.getReadyAt())
			    .canceledAt(order.getCanceledAt())
			    .items(itemDisassembler.fromPersistence(order.getItems()))
				.version(order.getVersion())
				.build();
	}

	private Billing billingFromPersistence(BillingEmbeddable entity) {
		if (entity == null) return null;
		Objects.requireNonNull(entity.getFirstName());
		Objects.requireNonNull(entity.getLastName());
		Objects.requireNonNull(entity.getDocument());
		Objects.requireNonNull(entity.getPhone());
		Objects.requireNonNull(entity.getEmail());
		return Billing.builder()
                        .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
                        .document(new Document(entity.getDocument()))
                        .phone(new Phone(entity.getPhone()))
                        .email(new Email(entity.getEmail()))
                        .address(addressFromPersistence(entity.getAddress()))
                        .build();
	}

	private Shipping shippingFromPersistence(ShippingEmbeddable entity) {
		if (entity == null) return null;
		Objects.requireNonNull(entity.getCost());
		Objects.requireNonNull(entity.getExpectedDeliveryDate());
		return Shipping.builder()
                     .cost(new Money(entity.getCost()))
                     .expectedDeliveryDate(entity.getExpectedDeliveryDate())
                     .recipient(recipientFromPersistence(entity.getRecipient()))
                     .address(addressFromPersistence(entity.getAddress()))
                     .build();
	}

	private Address addressFromPersistence(AddressEmbeddable entity) {
		if (entity == null) return null;
		Objects.requireNonNull(entity.getStreet());
		Objects.requireNonNull(entity.getNumber());
		Objects.requireNonNull(entity.getCity());
		Objects.requireNonNull(entity.getState());
		return Address.builder()
                    .street(entity.getStreet())
                    .number(entity.getNumber())
                    .complement(entity.getComplement())
                    .neighborhood(entity.getNeighborhood())
                    .city(entity.getCity())
                    .state(entity.getState())
                    .zipCode(new ZipCode(entity.getZipCode()))
                    .build();
	}

	private Recipient recipientFromPersistence(RecipientEmbeddable entity) {
		if (entity == null) return null;
		Objects.requireNonNull(entity.getFirstName());
		Objects.requireNonNull(entity.getLastName());
		Objects.requireNonNull(entity.getDocument());
		Objects.requireNonNull(entity.getPhone());
		return Recipient.builder()
		                .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
	                    .document(new Document(entity.getDocument()))
	                    .phone(new Phone(entity.getPhone()))
	                    .build();
	}
}
