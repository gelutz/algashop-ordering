package com.lutz.algashop.ordering.domain.entity.order.vo;

import com.lutz.algashop.ordering.domain.entity.customer.vo.Address;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
public record Shipping(
		@NonNull Recipient recipient,
		@NonNull Address address,
		@NonNull Money cost,
		@NonNull LocalDate expectedDeliveryDate
) {
}
