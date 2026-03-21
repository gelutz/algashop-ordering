package com.lutz.algashop.ordering.application.customer.management;

import com.lutz.algashop.ordering.application.commons.AddressData;
import com.lutz.algashop.ordering.application.utility.Mapper;
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
public class CustomerManagementApplicationService {
	private final CustomerRegistrationService customerRegistration;
	private final Customers customers;
	private final Mapper mapper;

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

		return mapper.convert(customer, CustomerOutput.class);
	}

	@Transactional
	public void update(@NonNull UUID customerId, @NonNull CustomerUpdateInput input) {
		Customer customer = customers.ofId(new CustomerId(customerId))
		                             .orElseThrow(CustomerNotFoundException::new);

		customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
		customer.changePhone(new Phone(input.getPhone()));

		if (Boolean.TRUE.equals(input.getPromotionNotificationsAllowed())) {
			customer.enablePromotionNotifications();
		} else {
			customer.disablePromotionNotifications();
		}

		customer.changeAddress(AddressData.toDomain(input.getAddress()));

		customers.add(customer);
	}
}
