package com.lutz.algashop.ordering.domain.order.shipping;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.ZipCode;

import java.time.LocalDate;

public interface ShippingCostService {
	CalculationResult calculate(CalculationRequest request);

	record CalculationRequest(ZipCode origin, ZipCode destination) {}
	record CalculationResult(Money cost, LocalDate expectedDate) {}
}
