package com.lutz.algashop.ordering.domain.commons;

import com.lutz.algashop.ordering.domain.FieldValidator;

public record Email(String email) {
	public Email {
		FieldValidator.requireValidEmail(email);
	}

	@Override
	public String toString() {
		return email();
	}
}
