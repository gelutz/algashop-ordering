package com.lutz.algashop.ordering.domain.order.shipping;

import com.lutz.algashop.ordering.domain.commons.ZipCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShippingCostServiceIT {
	@Autowired
	private ShippingCostService shippingCostService;

	@Autowired
	private OriginAddressService originAddressService;

	@Test
	void shouldCalculate() {
		ZipCode origin = originAddressService.originAddress().zipCode();
		ZipCode destination = new ZipCode("12345");

		ShippingCostService.CalculationResult result = shippingCostService.calculate(new ShippingCostService.CalculationRequest(
				origin,
				destination));

		Assertions.assertNotNull(result.cost());
		Assertions.assertNotNull(result.expectedDate());
	}

}