package com.lutz.algashop.ordering.domain.service;

import com.lutz.algashop.ordering.domain.entity.customer.vo.ZipCode;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;

import java.time.LocalDate;

public interface ShippingCostService {
	CalculationResult calculate(CalculationRequest request);

	record CalculationRequest(ZipCode origin, ZipCode destination) {}
	record CalculationResult(Money cost, LocalDate expectedDate) {}
}
