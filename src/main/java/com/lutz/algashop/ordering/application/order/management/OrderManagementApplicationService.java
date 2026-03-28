package com.lutz.algashop.ordering.application.order.management;

import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService {
	private final Orders orders;

	@Transactional
	public void cancel(Long rawOrderId) {
		Order order = orders.ofId(new OrderId(rawOrderId))
		                    .orElseThrow(OrderNotFoundException::new);
		order.cancel();
		orders.add(order);
	}

	@Transactional
	public void markAsPaid(Long rawOrderId) {
		Order order = orders.ofId(new OrderId(rawOrderId))
		                    .orElseThrow(OrderNotFoundException::new);
		order.markAsPaid();
		orders.add(order);
	}

	@Transactional
	public void markAsReady(Long rawOrderId) {
		Order order = orders.ofId(new OrderId(rawOrderId))
		                    .orElseThrow(OrderNotFoundException::new);
		order.markAsReady();
		orders.add(order);
	}
}
