package com.lutz.algashop.ordering.infrastructure.listener.customer;

import com.lutz.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.lutz.algashop.ordering.application.customer.notification.CustomerNotificationService.NotifyNewRegistrationInput;
import com.lutz.algashop.ordering.domain.customer.CustomerArchivedEvent;
import com.lutz.algashop.ordering.domain.customer.CustomerRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerEventListener {
	private final CustomerNotificationService notificationService;

	@EventListener
	public void listen(CustomerRegisteredEvent event) {
		log.info(event.toString());
		var input = new NotifyNewRegistrationInput(
				event.id().value(),
				event.fullName().firstName(),
				event.email().toString()
		);
		notificationService.notifyNewRegistration(input);
	}

	@EventListener
	public void listen(CustomerArchivedEvent event) {
		log.info(event.toString());
	}
}
