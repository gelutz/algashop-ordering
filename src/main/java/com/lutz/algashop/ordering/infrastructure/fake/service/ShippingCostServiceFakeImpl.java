package com.lutz.algashop.ordering.infrastructure.fake.service;

import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.service.ShippingCostService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ShippingCostServiceFakeImpl implements ShippingCostService {

	@Override
	public CalculationResult calculate(CalculationRequest request) {
		return new CalculationResult(new Money("5"), LocalDate.now().plusDays(5));
	}
}
