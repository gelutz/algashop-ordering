package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.Order;
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
		LoyaltyPoints expectedPoints = new LoyaltyPoints(points * CustomerLoyaltyPointsService.MULTIPLIER.value());
		sut.addPoints(customer, order);
		LoyaltyPoints result = customer.loyaltyPoints();

		Assertions.assertEquals(expectedPoints, result);
	}
}