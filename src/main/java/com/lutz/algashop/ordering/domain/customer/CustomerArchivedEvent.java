package com.lutz.algashop.ordering.domain.customer;

import java.time.OffsetDateTime;

public record CustomerArchivedEvent(
		CustomerId id,
		OffsetDateTime archivedAt
) {
}
