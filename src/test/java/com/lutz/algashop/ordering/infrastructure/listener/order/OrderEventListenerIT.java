package com.lutz.algashop.ordering.infrastructure.listener.order;

import com.lutz.algashop.ordering.application.customer.loyalty.CustomerLoyaltyPointsApplicationService;
import com.lutz.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.OrderReadyEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.OffsetDateTime;
import java.util.UUID;

@SpringBootTest
class OrderEventListenerIT {

	// Publisher do próprio spring. Oferece menos controle sobre quando os eventos são enviados
	// (não está em uma transaction como o nosso publisher)
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@MockitoSpyBean
	private OrderEventListener orderEventListener;

	@MockitoBean
	private CustomerLoyaltyPointsApplicationService loyaltyPointsApplicationService;

	@MockitoSpyBean
	private CustomerNotificationApplicationService notificationApplicationService;

	@Test
	void shouldListenForOrderReadyEvents() {
		applicationEventPublisher.publishEvent(
				new OrderReadyEvent(
						new OrderId(),
						new CustomerId(),
						OffsetDateTime.now()
				)
		);

		Mockito.verify(orderEventListener).listen(Mockito.any(OrderReadyEvent.class));
		Mockito.verify(loyaltyPointsApplicationService).addLoyaltyPoints(Mockito.any(UUID.class), Mockito.anyString());
	}
}

