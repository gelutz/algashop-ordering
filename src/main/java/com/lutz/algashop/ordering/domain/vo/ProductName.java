package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import com.lutz.algashop.ordering.domain.validation.FieldValidator;

import java.util.Objects;

public record ProductName(String value) {
	public ProductName {
		Objects.requireNonNull(value, ErrorMessages.Validation.fieldIsNullMessage("Product name"));
		FieldValidator.requireNonBlank(value, ErrorMessages.Validation.fieldIsNullMessage("Product name"));
	}

	@Override
	public String toString() {
		return value();
	}
}
