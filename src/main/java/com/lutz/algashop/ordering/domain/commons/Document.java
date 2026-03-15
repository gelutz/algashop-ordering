package com.lutz.algashop.ordering.domain.commons;

import com.lutz.algashop.ordering.domain.FieldValidator;

import java.util.Objects;

public record Document(String document) {
	public Document {
		Objects.requireNonNull(document, ErrorMessages.Fields.DOCUMENT_IS_NULL);
		FieldValidator.requireNonBlank(document, ErrorMessages.Fields.DOCUMENT_IS_BLANK);
	}

	@Override
	public String toString() {
		return document();
	}
}
