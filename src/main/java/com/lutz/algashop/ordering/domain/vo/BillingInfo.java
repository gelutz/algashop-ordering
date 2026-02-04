package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.entity.customer.vo.Address;
import com.lutz.algashop.ordering.domain.entity.customer.vo.Document;
import com.lutz.algashop.ordering.domain.entity.customer.vo.FullName;
import com.lutz.algashop.ordering.domain.entity.customer.vo.Phone;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record BillingInfo(
	@NonNull FullName fullName,
	@NonNull Document document,
	@NonNull Phone phone,
	@NonNull Address address
) {
}
