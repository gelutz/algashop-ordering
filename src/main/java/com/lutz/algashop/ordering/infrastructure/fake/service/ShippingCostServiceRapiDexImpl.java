package com.lutz.algashop.ordering.infrastructure.fake.service;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.order.shipping.ShippingCostService;
import com.lutz.algashop.ordering.infrastructure.client.rapidex.DeliveryCostRequest;
import com.lutz.algashop.ordering.infrastructure.client.rapidex.DeliveryCostResponse;
import com.lutz.algashop.ordering.infrastructure.client.rapidex.RapiDexAPIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "algashop.integrations.shipping.provider", havingValue = "RAPIDEX")
public class ShippingCostServiceRapiDexImpl implements ShippingCostService {
	private final RapiDexAPIClient rapiDexAPIClient;

	public CalculationResult calculate(CalculationRequest request) {
		DeliveryCostResponse response = rapiDexAPIClient.calculate(new DeliveryCostRequest(
				request.origin().value(),
				request.destination().value()
		));

		LocalDate expectedDeliveryDate = LocalDate.now().plusDays(response.getEstimatedDaysToDeliver());

		return new CalculationResult(
				new Money(response.getDeliveryCost()),
				expectedDeliveryDate
		);
	}
}
