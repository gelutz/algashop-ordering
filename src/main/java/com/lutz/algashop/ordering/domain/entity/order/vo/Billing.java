package com.lutz.algashop.ordering.domain.entity.order.vo;

import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
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
