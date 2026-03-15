package com.lutz.algashop.ordering.domain.commons;

import com.lutz.algashop.ordering.domain.FieldValidator;

import java.util.Objects;

public record ZipCode(String value) {
	public ZipCode {
		Objects.requireNonNull(value);
		FieldValidator.requireNonBlank(value);
		if (value.length() < 5) throw new IllegalArgumentException();
	}

	@Override
	public String toString() {
		return value;
	}
}
