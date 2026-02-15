package com.lutz.algashop.ordering.infrastructure.persistence.assembler;

import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.Address;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.AddressEmbeddable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerPersistenceEntityAssembler {

	public CustomerPersistenceEntity fromDomain(Customer customer) {
		return merge(new CustomerPersistenceEntity(), customer);
	}

	public CustomerPersistenceEntity merge(CustomerPersistenceEntity customerPersistenceEntity, Customer customer) {
		customerPersistenceEntity.setId(customer.id().value());
		customerPersistenceEntity.setFirstName(customer.fullName().firstName());
		customerPersistenceEntity.setLastName(customer.fullName().lastName());
		customerPersistenceEntity.setBirthdate(customer.birthdate() != null ? customer.birthdate().date() : null);
		customerPersistenceEntity.setEmail(customer.email().toString());
		customerPersistenceEntity.setPhone(customer.phone().toString());
		customerPersistenceEntity.setDocument(customer.document().toString());
		customerPersistenceEntity.setLoyaltyPoints(customer.loyaltyPoints().value());
		customerPersistenceEntity.setPromotionNotificationAllowed(customer.promotionNotificationAllowed());
		customerPersistenceEntity.setArchived(customer.archived());
		customerPersistenceEntity.setRegisteredAt(customer.registeredAt());
		customerPersistenceEntity.setArchivedAt(customer.archivedAt());
		customerPersistenceEntity.setAddress(addressFromDomain(customer.address()));
		customerPersistenceEntity.setVersion(customer.version());

		return customerPersistenceEntity;
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
}
