package com.lutz.algashop.ordering.domain.order.specification;

import com.lutz.algashop.ordering.domain.Specification;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.LoyaltyPoints;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerHasEnoughLoyaltyPointsSpecification implements Specification<Customer> {
	private final LoyaltyPoints expectedLoyaltyPoints;

	@Override
	public boolean isSatisfiedBy(Customer customer) {
		return customer.loyaltyPoints().compareTo(expectedLoyaltyPoints) >= 0;
	}
}
