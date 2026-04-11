package com.lutz.algashop.ordering.infrastructure.listener.order;

import com.lutz.algashop.ordering.application.customer.loyalty.CustomerLoyaltyPointsApplicationService;
import com.lutz.algashop.ordering.domain.order.OrderCanceledEvent;
import com.lutz.algashop.ordering.domain.order.OrderPaidEvent;
import com.lutz.algashop.ordering.domain.order.OrderPlacedEvent;
import com.lutz.algashop.ordering.domain.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
	private final CustomerLoyaltyPointsApplicationService loyaltyPointsApplicationService;

	@EventListener
	public void listen(OrderPlacedEvent event) {

	}

	@EventListener
	public void listen(OrderPaidEvent event) {

	}

	@EventListener
	public void listen(OrderReadyEvent event) {
		loyaltyPointsApplicationService.addLoyaltyPoints(event.customerId().value(), event.id().toString());
	}

	@EventListener
	public void listen(OrderCanceledEvent event) {
	}
}
