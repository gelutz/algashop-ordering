package com.lutz.algashop.ordering.domain.order.specification;

import com.lutz.algashop.ordering.domain.Specification;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.LoyaltyPoints;
import com.lutz.algashop.ordering.domain.order.Orders;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerHasFreeShippingSpecification implements Specification<Customer> {
	private final CustomerHasOrderedEnoughAtYearSpecification customerHasOrderedEnoughAtYear;
	private final CustomerHasEnoughLoyaltyPointsSpecification basicHasEnoughPoints;
	private final CustomerHasEnoughLoyaltyPointsSpecification premiumHasEnoughPoints;

	public CustomerHasFreeShippingSpecification(
			Orders orders,
			LoyaltyPoints basicLoyaltyPoints,
			long salesQuantityForFreeShipping,
			LoyaltyPoints premiumLoyaltyPoints
	) {
		this.customerHasOrderedEnoughAtYear = new CustomerHasOrderedEnoughAtYearSpecification(orders,
		                                                                                      salesQuantityForFreeShipping
		);

		this.basicHasEnoughPoints = new CustomerHasEnoughLoyaltyPointsSpecification(basicLoyaltyPoints);
		this.premiumHasEnoughPoints = new CustomerHasEnoughLoyaltyPointsSpecification(premiumLoyaltyPoints);
	}


	@Override
	public boolean isSatisfiedBy(Customer customer) {
		return basicHasEnoughPoints
				.and(customerHasOrderedEnoughAtYear)
				.or(premiumHasEnoughPoints)
				.isSatisfiedBy(customer);
	}
}
