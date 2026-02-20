package com.lutz.algashop.ordering.domain.repository;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;

import java.time.Year;
import java.util.List;

public interface Orders extends Repository<Order, OrderId> {
	List<Order> placedByCustomerInYear(CustomerId customerId, Year year);
}
