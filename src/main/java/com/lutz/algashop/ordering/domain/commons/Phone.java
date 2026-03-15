package com.lutz.algashop.ordering.domain.commons;

import com.lutz.algashop.ordering.domain.FieldValidator;

import java.util.Objects;

public record Phone(String phone) {
	public Phone {
		Objects.requireNonNull(phone, ErrorMessages.Fields.PHONE_IS_NULL);
		FieldValidator.requireNonBlank(phone, ErrorMessages.Fields.PHONE_IS_BLANK);
	}

	@Override
	public String toString() {
		return phone();
	}
}
