package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BirthdateTest {

	@Test
	void givenNullDateShouldThrowException() {
		NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> new Birthdate(null));
		Assertions.assertEquals(ErrorMessages.Validation.BIRTHDATE_MUST_NOT_BE_NULL, exception.getMessage());
	}

	@Test
	void givenFutureDateShouldThrowException() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> new Birthdate(LocalDate.now().plusYears(1)));

		Assertions.assertEquals(ErrorMessages.Validation.BIRTHDATE_MUST_IN_PAST, exception.getMessage());
	}
}