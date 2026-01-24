package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;

import java.util.Objects;

public record Phone(String phone) {
	public Phone {
		Objects.requireNonNull(phone, ErrorMessages.Validation.PHONE_IS_NULL);
		if (phone.isBlank()) throw new IllegalArgumentException(ErrorMessages.Validation.PHONE_IS_BLANK);
	}

	@Override
	public String toString() {
		return phone();
	}
}
