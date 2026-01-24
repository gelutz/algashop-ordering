package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
	@Test
	void givenNullEmailShouldThrowNullPointerException() {
		NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> new Email(null));
		Assertions.assertEquals(ErrorMessages.Validation.EMAIL_IS_NULL, exception.getMessage());
	}

	@Test
	void givenBlankEmailShouldThrowIllegalArgumentException() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Email(""));
		Assertions.assertEquals(ErrorMessages.Validation.EMAIL_IS_BLANK, exception.getMessage());
	}

	@Test
	void givenInvalidEmailShouldThrowIllegalArgumentException() {
		IllegalArgumentException exception = Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> new Email("invalid@"));
		Assertions.assertEquals(ErrorMessages.Validation.EMAIL_IS_INVALID, exception.getMessage());
	}

	@Test
	void givenValidEmailShouldConstruct() {
		String valid = "valid@email.com";
		Email email = new Email("valid@email.com");

		Assertions.assertEquals(valid, email.toString());
	}
}