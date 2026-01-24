package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.validation.FieldValidator;

public record Email(String email) {
	public Email {
		FieldValidator.requireValidEmail(email);
	}

	@Override
	public String toString() {
		return email();
	}
}
