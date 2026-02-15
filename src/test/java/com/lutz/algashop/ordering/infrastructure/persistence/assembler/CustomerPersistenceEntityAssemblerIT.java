package com.lutz.algashop.ordering.infrastructure.persistence.assembler;

import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerPersistenceEntityAssemblerIT {
	private CustomerPersistenceEntityAssembler sut;

	@BeforeEach
	void setup() {
		sut = new CustomerPersistenceEntityAssembler();
	}

	@Test
	void givenDomainObjectShouldConvertToPersistenceObject() {
		Customer customerStub = CustomerTestBuilder.aCustomer().build();

		CustomerPersistenceEntity result = sut.fromDomain(customerStub);

		assertEquals(result.getId(), customerStub.id().value());
		assertEquals(result.getFirstName(), customerStub.fullName().firstName());
		assertEquals(result.getLastName(), customerStub.fullName().lastName());
		assertEquals(result.getBirthdate(), customerStub.birthdate().date());
		assertEquals(result.getEmail(), customerStub.email().toString());
		assertEquals(result.getPhone(), customerStub.phone().toString());
		assertEquals(result.getDocument(), customerStub.document().toString());
		assertEquals(result.getLoyaltyPoints(), customerStub.loyaltyPoints().value());
		assertEquals(result.getPromotionNotificationAllowed(), customerStub.promotionNotificationAllowed());
		assertEquals(result.getArchived(), customerStub.archived());
		assertEquals(result.getRegisteredAt(), customerStub.registeredAt());
		assertEquals(result.getArchivedAt(), customerStub.archivedAt());
		assertNotNull(result.getAddress());
		assertEquals(result.getAddress().getStreet(), customerStub.address().street());
		assertEquals(result.getAddress().getNumber(), customerStub.address().number());
		assertEquals(result.getAddress().getCity(), customerStub.address().city());
		assertEquals(result.getAddress().getState(), customerStub.address().state());
		assertEquals(result.getAddress().getZipCode(), customerStub.address().zipCode().toString());
	}

	@Test
	void givenPersistenceObjectAndDomainObjectShouldMerge() {
		Customer customerStub = CustomerTestBuilder.aCustomer().build();
		CustomerPersistenceEntity base = new CustomerPersistenceEntity();

		CustomerPersistenceEntity result = sut.merge(base, customerStub);

		assertEquals(result.getId(), customerStub.id().value());
		assertEquals(result.getFirstName(), customerStub.fullName().firstName());
		assertEquals(result.getLastName(), customerStub.fullName().lastName());
		assertEquals(result.getBirthdate(), customerStub.birthdate().date());
		assertEquals(result.getEmail(), customerStub.email().toString());
		assertEquals(result.getPhone(), customerStub.phone().toString());
		assertEquals(result.getDocument(), customerStub.document().toString());
		assertEquals(result.getLoyaltyPoints(), customerStub.loyaltyPoints().value());
		assertEquals(result.getPromotionNotificationAllowed(), customerStub.promotionNotificationAllowed());
		assertEquals(result.getArchived(), customerStub.archived());
		assertEquals(result.getRegisteredAt(), customerStub.registeredAt());
		assertEquals(result.getArchivedAt(), customerStub.archivedAt());
		assertNotNull(result.getAddress());
		assertEquals(result.getAddress().getStreet(), customerStub.address().street());
		assertEquals(result.getAddress().getNumber(), customerStub.address().number());
		assertEquals(result.getAddress().getCity(), customerStub.address().city());
		assertEquals(result.getAddress().getState(), customerStub.address().state());
		assertEquals(result.getAddress().getZipCode(), customerStub.address().zipCode().toString());
	}
}
