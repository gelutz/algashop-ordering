package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.commons.Email;
import com.lutz.algashop.ordering.domain.commons.FullName;

import java.time.OffsetDateTime;

public record CustomerRegisteredEvent(
		CustomerId id,
		FullName fullName,
		Email email,
		OffsetDateTime registeredAt
) {
}
