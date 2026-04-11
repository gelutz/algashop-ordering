package com.lutz.algashop.ordering.domain.order;

import com.lutz.algashop.ordering.domain.customer.CustomerId;

import java.time.OffsetDateTime;

public record OrderCanceledEvent(
		OrderId id,
		CustomerId customerId,
		OffsetDateTime canceledAt
) {
}
