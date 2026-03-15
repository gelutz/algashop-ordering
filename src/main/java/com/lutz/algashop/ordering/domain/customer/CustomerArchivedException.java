package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.commons.ErrorMessages;

public class CustomerArchivedException extends DomainException {
	public CustomerArchivedException() {
		super(ErrorMessages.CUSTOMER_ARCHIVED);
	}

	public CustomerArchivedException(Throwable cause) {
		super(ErrorMessages.CUSTOMER_ARCHIVED, cause);
	}
}
