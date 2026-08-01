package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.DomainEntityNotFoundException;
import com.lutz.algashop.ordering.domain.commons.ErrorMessages;

public class CustomerNotFoundException extends DomainEntityNotFoundException {
	public CustomerNotFoundException() {
		super(ErrorMessages.CUSTOMER_NOT_FOUND);
	}

	public CustomerNotFoundException(Throwable cause) {
		super(ErrorMessages.CUSTOMER_NOT_FOUND, cause);
	}
}
