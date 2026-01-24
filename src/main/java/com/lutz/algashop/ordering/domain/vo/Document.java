package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;

import java.util.Objects;

public record Document(String document) {
	public Document {
		Objects.requireNonNull(document, ErrorMessages.Validation.DOCUMENT_IS_NULL);
		if (document.isBlank()) throw new IllegalArgumentException(ErrorMessages.Validation.DOCUMENT_IS_BLANK);
	}

	@Override
	public String toString() {
		return document();
	}
}
