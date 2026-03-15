package com.lutz.algashop.ordering.infrastructure.fake.service;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.order.shipping.ShippingCostService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(name = "algashop.integrations.shipping.provider", havingValue = "FAKE")
public class ShippingCostServiceFakeImpl implements ShippingCostService {

	@Override
	public CalculationResult calculate(CalculationRequest request) {
		return new CalculationResult(new Money("5"), LocalDate.now().plusDays(5));
	}
}
