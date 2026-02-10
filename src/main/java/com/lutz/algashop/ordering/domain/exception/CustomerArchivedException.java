package com.lutz.algashop.ordering.domain.exception;

import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;

public class CustomerArchivedException extends DomainException {
	public CustomerArchivedException() {
		super(ErrorMessages.CUSTOMER_ARCHIVED);
	}

	public CustomerArchivedException(Throwable cause) {
		super(ErrorMessages.CUSTOMER_ARCHIVED, cause);
	}
}
