package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.commons.ErrorMessages;

public class CustomerNotFoundException extends DomainException {
	public CustomerNotFoundException() {
		super(ErrorMessages.CUSTOMER_ARCHIVED);
	}

	public CustomerNotFoundException(Throwable cause) {
		super(ErrorMessages.CUSTOMER_ARCHIVED, cause);
	}
}
