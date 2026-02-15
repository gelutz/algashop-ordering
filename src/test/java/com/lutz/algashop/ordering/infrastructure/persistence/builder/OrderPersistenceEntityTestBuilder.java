package com.lutz.algashop.ordering.infrastructure.persistence.builder;

import com.lutz.algashop.ordering.domain.utils.IdGenerator;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public class OrderPersistenceEntityTestBuilder {

	public static OrderPersistenceEntity.OrderPersistenceEntityBuilder existing() {
		return OrderPersistenceEntity.builder()
		                      .id(IdGenerator.generateTSID().toLong())
		                      .customer(CustomerPersistenceEntityTestBuilder.existing().build())
		                      .totalItems(2)
		                      .totalAmount(new BigDecimal((100)))
		                      .status("DRAFT")
		                      .paymentMethod("CREDIT_CARD")
		                      .placedAt(OffsetDateTime.now())
                              .items(Set.of(anItem().build()));
	}

	public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder anItem() {
		return OrderItemPersistenceEntity.builder()
		                                 .id(IdGenerator.generateTSID().toLong())
		                                 .productId(IdGenerator.generateTimeBasedUUID())
		                                 .productName("Random product")
		                                 .price(new BigDecimal(10))
		                                 .quantity(1)
		                                 .totalAmount(new BigDecimal(10));
	}
}
