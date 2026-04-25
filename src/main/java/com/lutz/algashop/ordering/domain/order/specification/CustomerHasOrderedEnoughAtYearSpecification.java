package com.lutz.algashop.ordering.domain.order.specification;

import com.lutz.algashop.ordering.domain.Specification;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.order.Orders;
import lombok.RequiredArgsConstructor;

import java.time.Year;

@RequiredArgsConstructor
public class CustomerHasOrderedEnoughAtYearSpecification implements Specification<Customer> {
	private final Orders orders;

	private final long expectedOrderCount;

	@Override
	public boolean isSatisfiedBy(Customer customer) {
		return orders.salesQuantityByCustomerInYear(customer.id(), Year.now()) >= expectedOrderCount;
	}
}
