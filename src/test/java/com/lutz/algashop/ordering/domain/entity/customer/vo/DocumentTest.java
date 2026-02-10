package com.lutz.algashop.ordering.domain.entity.customer.vo;

import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentTest {

	@Test
	void givenNullDocumentShouldThrowNullPointerException() {
		NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> new Document(null));
		Assertions.assertEquals(ErrorMessages.Fields.DOCUMENT_IS_NULL, exception.getMessage());
	}

	@Test
	void givenBlankDocumentShouldThrowIllegalArgumentException() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Document(""));
		Assertions.assertEquals(ErrorMessages.Fields.DOCUMENT_IS_BLANK, exception.getMessage());
	}
}