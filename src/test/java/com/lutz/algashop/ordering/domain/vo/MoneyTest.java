package com.lutz.algashop.ordering.domain.vo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

class MoneyTest {
	BigDecimal valueWithScale2 = BigDecimal.ONE.setScale(2, RoundingMode.HALF_EVEN);

	@Test
	void shouldBeConstructableWithBigDecimal() {
		Money sut = new Money(BigDecimal.ONE);
		Assertions.assertEquals(valueWithScale2, sut.value());
	}

	@Test
	void shouldBeConstructableWithString() {
		Money sut = new Money("1");

		Assertions.assertEquals(valueWithScale2, sut.value());
	}

	@Test
	void shouldSetValueScaleTo2() {
		Money sut = new Money(BigDecimal.ONE);

		Assertions.assertEquals(2, sut.value().scale());
	}

	@Test
	void toStringMethodShouldReturnValueWithDecimalPlaces() {
		Money sut = new Money(BigDecimal.ONE);

		Assertions.assertEquals("1.00", sut.value().toString());
	}

	@Test
	void givenNegativeInputShouldThrowIllegalArgumentException() {
		BigDecimal negative = BigDecimal.valueOf(-1L);
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Money(negative));
	}

//	@Test
//	void
}