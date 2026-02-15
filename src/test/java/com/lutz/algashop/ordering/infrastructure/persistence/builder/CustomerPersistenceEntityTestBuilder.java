package com.lutz.algashop.ordering.infrastructure.persistence.builder;

import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.AddressEmbeddable;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerPersistenceEntityTestBuilder {

	public static CustomerPersistenceEntity.CustomerPersistenceEntityBuilder existing() {
		return CustomerPersistenceEntity.builder()
				.id(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value())
				.firstName("John")
				.lastName("Doe")
				.birthdate(LocalDate.now().minusYears(10))
				.email("valid@email.com")
				.phone("123123123")
				.document("123123123")
				.loyaltyPoints(0)
				.promotionNotificationAllowed(false)
				.archived(false)
				.registeredAt(OffsetDateTime.now())
				.archivedAt(null)
				.address(anAddress().build());
	}

	public static AddressEmbeddable.AddressEmbeddableBuilder anAddress() {
		return AddressEmbeddable.builder()
				.street("This street")
				.number("102")
				.complement("near the thing")
				.neighborhood("that one")
				.city("Brooklyn")
				.state("New York")
				.zipCode("123123");
	}
}
