package com.lutz.algashop.ordering.infrastructure.persistence.builder;

import com.lutz.algashop.ordering.domain.utils.IdGenerator;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderPersistenceEntityTestBuilder {

	public static OrderPersistenceEntity.OrderPersistenceEntityBuilder existing() {
		return OrderPersistenceEntity.builder()
		                      .id(IdGenerator.generateTSID().toLong())
		                      .customerId(IdGenerator.generateTimeBasedUUID())
		                      .totalItems(2)
		                      .totalAmount(new BigDecimal((100)))
		                      .status("DRAFT")
		                      .paymentMethod("CREDIT_CARD")
		                      .placedAt(OffsetDateTime.now());
	}
}
