package com.lutz.algashop.ordering.domain.service;

import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.LoyaltyPoints;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerLoyaltyPointsServiceTest {
	private CustomerLoyaltyPointsService sut = new CustomerLoyaltyPointsService();
	private final Money threshold = CustomerLoyaltyPointsService.VALUE_PER_POINT;

	@Test
	void givenValidCustomAndOrderShouldAddPoints() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		Order order = OrderTestBuilder
				.aFilledDraftOrder()
				.withTotalAmount(new Money("1000"))
				.build();
		order.place();
		order.markAsPaid();
		order.markAsReady();

		int points = order.totalAmount().divide(threshold).value().intValue();
		LoyaltyPoints expectedPoints = new LoyaltyPoints(points);
		sut.addPoints(customer, order);
		LoyaltyPoints result = customer.loyaltyPoints();

		Assertions.assertEquals(expectedPoints, result);
	}
}