package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import com.lutz.algashop.ordering.domain.validation.FieldValidator;

import java.util.Objects;

public record Phone(String phone) {
	public Phone {
		Objects.requireNonNull(phone, ErrorMessages.Validation.PHONE_IS_NULL);
		FieldValidator.requireNonBlank(phone, ErrorMessages.Validation.PHONE_IS_BLANK);
	}

	@Override
	public String toString() {
		return phone();
	}
}
