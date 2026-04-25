package com.lutz.algashop.ordering.infrastructure.beans;

import com.lutz.algashop.ordering.domain.customer.LoyaltyPoints;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.specification.CustomerHasFreeShippingSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpecificationBeansConfig {

	@Bean
	public CustomerHasFreeShippingSpecification customerHasFreeShippingSpecification(Orders orders) {
		return new CustomerHasFreeShippingSpecification(
				orders,
				new LoyaltyPoints(100),
				2,
				new LoyaltyPoints(200)
		);
	}
}
