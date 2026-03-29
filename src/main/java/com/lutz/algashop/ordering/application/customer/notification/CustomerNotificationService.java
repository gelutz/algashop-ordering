package com.lutz.algashop.ordering.application.customer.notification;

import java.util.UUID;

public interface CustomerNotificationService {
	void notifyNewRegistration(UUID id);
}
