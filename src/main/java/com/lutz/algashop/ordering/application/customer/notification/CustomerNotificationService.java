package com.lutz.algashop.ordering.application.customer.notification;

import java.util.UUID;

public interface CustomerNotificationService {
	void notifyNewRegistration(NotifyNewRegistrationInput input);

	record NotifyNewRegistrationInput(UUID input, String firstName, String email) {}

}
