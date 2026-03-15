package com.lutz.algashop.ordering.application.customer.management;

import com.lutz.algashop.ordering.application.commons.AddressData;
import com.lutz.algashop.ordering.domain.commons.Document;
import com.lutz.algashop.ordering.domain.commons.Email;
import com.lutz.algashop.ordering.domain.commons.FullName;
import com.lutz.algashop.ordering.domain.commons.Phone;
import com.lutz.algashop.ordering.domain.customer.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerManagementService {
	private final CustomerRegistrationService customerRegistration;
	private final Customers customers;

	@Transactional
	public UUID create(@NonNull CustomerInput input) {
		Customer registered = customerRegistration.register(
				new FullName(input.getFirstName(), input.getLastName()),
				new Birthdate(input.getBirthdate()),
				new Email(input.getEmail()),
				new Phone(input.getPhone()),
				new Document(input.getDocument()),
				AddressData.toDomain(input.getAddress()),
				input.getPromotionNotificationsAllowed()
		                                                 );
		customers.add(registered);

		return registered.id().value();
	}

	@Transactional(readOnly = true)
	public CustomerOutput findById(@NonNull UUID id) {
		Customer customer = customers.ofId(new CustomerId(id)).orElseThrow(CustomerNotFoundException::new);

		return CustomerOutput.builder()
		              .id(customer.id().value())
		              .firstName(customer.fullName().firstName())
		              .lastName(customer.fullName().lastName())
		              .email(customer.email().toString())
		              .phone(customer.phone().toString())
		              .document(customer.document().toString())
		              .birthdate(customer.birthdate().date())
		              .promotionNotificationAllowed(customer.promotionNotificationAllowed())
		              .archived(customer.archived())
		              .loyaltyPoints(customer.loyaltyPoints().value())
		              .registeredAt(customer.registeredAt())
		              .archivedAt(customer.archivedAt())
		              .address(AddressData.fromDomain(customer.address()))
		              .build();
	}
}
