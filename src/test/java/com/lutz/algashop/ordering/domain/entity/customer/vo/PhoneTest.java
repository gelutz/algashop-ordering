package com.lutz.algashop.ordering.domain.entity.customer.vo;

import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PhoneTest {
	@Test
	void givenNullPhoneShouldThrowNullPointerException() {
		NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> new Phone(null));
		Assertions.assertEquals(ErrorMessages.Fields.PHONE_IS_NULL, exception.getMessage());
	}

	@Test
	void givenBlankPhoneShouldThrowIllegalArgumentException() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Phone(""));
		Assertions.assertEquals(ErrorMessages.Fields.PHONE_IS_BLANK, exception.getMessage());
	}
}