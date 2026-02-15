package com.lutz.algashop.ordering.infrastructure.persistence.disassembler;

import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.AddressEmbeddable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerPersistenceEntityDisassembler {

	public Customer fromPersistence(CustomerPersistenceEntity entity) {
		return Customer.existing()
				.id(new CustomerId(entity.getId()))
				.fullName(new FullName(entity.getFirstName(), entity.getLastName()))
				.birthDate(entity.getBirthdate() != null ? new Birthdate(entity.getBirthdate()) : null)
				.email(new Email(entity.getEmail()))
				.phone(new Phone(entity.getPhone()))
				.document(new Document(entity.getDocument()))
				.loyaltyPoints(new LoyaltyPoints(entity.getLoyaltyPoints()))
				.promotionNotificationAllowed(entity.getPromotionNotificationAllowed())
				.archived(entity.getArchived())
				.registeredAt(entity.getRegisteredAt())
				.archivedAt(entity.getArchivedAt())
				.address(addressFromPersistence(entity.getAddress()))
				.version(entity.getVersion())
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
}
