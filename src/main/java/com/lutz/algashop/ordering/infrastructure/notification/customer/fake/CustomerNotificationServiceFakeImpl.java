package com.lutz.algashop.ordering.infrastructure.notification.customer.fake;

import com.lutz.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.customer.CustomerNotFoundException;
import com.lutz.algashop.ordering.domain.customer.Customers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerNotificationServiceFakeImpl implements CustomerNotificationService  {
	private final Customers customers;
	@Override
	public void notifyNewRegistration(UUID id) {
		Customer customer = customers.ofId(new CustomerId(id)).orElseThrow(CustomerNotFoundException::new);
		log.info("Enviando email de boas vindas para {}. Bem vindo!", customer.email());
	}
}
