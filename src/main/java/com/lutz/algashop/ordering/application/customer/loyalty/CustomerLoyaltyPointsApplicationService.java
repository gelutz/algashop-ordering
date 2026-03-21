package com.lutz.algashop.ordering.application.customer.loyalty;

import com.lutz.algashop.ordering.domain.customer.*;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerLoyaltyPointsApplicationService {
	private final CustomerLoyaltyPointsService customerLoyaltyPointsService;
	private final Customers customers;
	private final Orders orders;

	@Transactional
	public void addLoyaltyPoints(@NonNull UUID rawCustomerId, @NonNull String rawOrderId) {
		Order order = orders.ofId(new OrderId(rawOrderId))
		                    .orElseThrow(OrderNotFoundException::new);
		Customer customer = customers.ofId(new CustomerId(rawCustomerId))
		                             .orElseThrow(CustomerNotFoundException::new);

		customerLoyaltyPointsService.addPoints(customer, order);

		customers.add(customer);
	}
}
