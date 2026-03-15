package com.lutz.algashop.ordering.infrastructure.persistence.customer;

import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntityDisassembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerPersistenceEntityDisassemblerIT {

	private CustomerPersistenceEntityDisassembler sut;

	@BeforeEach
	void setup() {
		sut = new CustomerPersistenceEntityDisassembler();
	}

	@Test
	void givenPersistenceObjectShouldConvertToDomainObject() {
		CustomerPersistenceEntity entity = CustomerPersistenceEntityTestBuilder.existing().build();

		Customer result = sut.fromPersistence(entity);

		assertEquals(result.id(), new CustomerId(entity.getId()));
		assertEquals(result.fullName().firstName(), entity.getFirstName());
		assertEquals(result.fullName().lastName(), entity.getLastName());
		assertEquals(result.birthdate().date(), entity.getBirthdate());
		assertEquals(result.email().toString(), entity.getEmail());
		assertEquals(result.phone().toString(), entity.getPhone());
		assertEquals(result.document().toString(), entity.getDocument());
		assertEquals(result.loyaltyPoints().value(), entity.getLoyaltyPoints());
		assertEquals(result.promotionNotificationAllowed(), entity.getPromotionNotificationAllowed());
		assertEquals(result.archived(), entity.getArchived());
		assertEquals(result.registeredAt(), entity.getRegisteredAt());
		assertEquals(result.archivedAt(), entity.getArchivedAt());
		assertNotNull(result.address());
		assertEquals(result.address().street(), entity.getAddress().getStreet());
		assertEquals(result.address().number(), entity.getAddress().getNumber());
		assertEquals(result.address().city(), entity.getAddress().getCity());
		assertEquals(result.address().state(), entity.getAddress().getState());
		assertEquals(result.address().zipCode().toString(), entity.getAddress().getZipCode());
	}
}
