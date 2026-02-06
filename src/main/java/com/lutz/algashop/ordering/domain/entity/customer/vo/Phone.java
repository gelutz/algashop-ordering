package com.lutz.algashop.ordering.domain.entity.customer.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import com.lutz.algashop.ordering.domain.validation.FieldValidator;

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
