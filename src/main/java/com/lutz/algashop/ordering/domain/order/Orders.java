package com.lutz.algashop.ordering.domain.order;

import com.lutz.algashop.ordering.domain.Repository;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.order.entity.Order;

import java.time.Year;
import java.util.List;

public interface Orders extends Repository<Order, OrderId> {
	List<Order> placedByCustomerInYear(CustomerId customerId, Year year);
	long salesQuantityByCustomerInYear(CustomerId customerId, Year year);
	Money totalSoldForCustomer(CustomerId customerId);
}
