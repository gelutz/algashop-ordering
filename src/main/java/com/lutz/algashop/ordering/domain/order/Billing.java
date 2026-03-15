package com.lutz.algashop.ordering.domain.order;

import com.lutz.algashop.ordering.domain.commons.*;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Billing(
	@NonNull FullName fullName,
	@NonNull Document document,
	@NonNull Phone phone,
	@NonNull Email email,
	@NonNull Address address
) {
}
