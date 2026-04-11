package com.lutz.algashop.ordering.infrastructure.listener.customer;

import com.lutz.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.lutz.algashop.ordering.domain.commons.Email;
import com.lutz.algashop.ordering.domain.commons.FullName;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.customer.CustomerRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.OffsetDateTime;

@SpringBootTest
class CustomerEventListenerIT {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@MockitoSpyBean
	private CustomerEventListener customerEventListener;

	@MockitoSpyBean
	private CustomerNotificationApplicationService notificationApplicationService;

	@Test
	void shouldListenForCustomerReadyEvents() {
		applicationEventPublisher.publishEvent(
				new CustomerRegisteredEvent(
						new CustomerId(),
						new FullName("Teste", "Teste"),
						new Email("teste@teste.com"),
						OffsetDateTime.now()
				)
		);

		Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));
		Mockito.verify(notificationApplicationService).notifyNewRegistration(
				Mockito.any(CustomerNotificationApplicationService.NotifyNewRegistrationInput.class)
		);
	}

	@Test
	void testListen() {
	}

	@Test
	void testListen1() {
	}
}