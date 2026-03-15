package com.lutz.algashop.ordering.domain.order.shipping;

import com.lutz.algashop.ordering.domain.commons.Address;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.order.Recipient;
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
