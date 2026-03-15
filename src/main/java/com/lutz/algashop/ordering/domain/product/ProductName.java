package com.lutz.algashop.ordering.domain.product;

import com.lutz.algashop.ordering.domain.FieldValidator;
import com.lutz.algashop.ordering.domain.commons.ErrorMessages;

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
