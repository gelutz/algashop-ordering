package com.lutz.algashop.ordering.domain.order;

import com.lutz.algashop.ordering.domain.customer.CustomerId;

import java.time.OffsetDateTime;

public record OrderPlacedEvent(
		OrderId id,
		CustomerId customerId,
		OffsetDateTime placedAt
) {
}
