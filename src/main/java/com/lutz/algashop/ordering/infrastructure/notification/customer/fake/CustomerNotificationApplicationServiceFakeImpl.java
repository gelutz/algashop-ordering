package com.lutz.algashop.ordering.infrastructure.notification.customer.fake;

import com.lutz.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerNotificationApplicationServiceFakeImpl implements CustomerNotificationApplicationService {
	@Override
	public void notifyNewRegistration(NotifyNewRegistrationInput input) {
		log.info("Enviando email de boas vindas para {}. Bem vindo, {}!", input.email(), input.firstName());
	}
}
