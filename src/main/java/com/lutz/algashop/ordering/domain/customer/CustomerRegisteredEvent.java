package com.lutz.algashop.ordering.domain.customer;

import java.time.OffsetDateTime;

public record CustomerRegisteredEvent(
		CustomerId id,
		OffsetDateTime registeredAt
) {
}
