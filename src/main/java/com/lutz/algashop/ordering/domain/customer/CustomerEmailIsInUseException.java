package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.DomainException;

public class CustomerEmailIsInUseException extends DomainException {
	public CustomerEmailIsInUseException() {
		super("Customer email is already in use.");
	}

	public CustomerEmailIsInUseException(Throwable cause) {
		super("Customer email is already in use.", cause);
	}
}
