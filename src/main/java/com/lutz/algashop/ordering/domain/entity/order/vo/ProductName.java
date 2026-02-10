package com.lutz.algashop.ordering.domain.entity.order.vo;

import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;
import com.lutz.algashop.ordering.domain.validation.FieldValidator;

import java.util.Objects;

public record ProductName(String value) {
	public ProductName {
		Objects.requireNonNull(value, ErrorMessages.Fields.fieldIsNullMessage("Product name"));
		FieldValidator.requireNonBlank(value, ErrorMessages.Fields.fieldIsNullMessage("Product name"));
	}

	@Override
	public String toString() {
		return value();
	}
}
