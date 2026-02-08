package com.lutz.algashop.ordering.domain.entity.order.vo;

import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
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

		Assertions.assertEquals("1.00", sut.toString());
	}

	@Test
	void givenNegativeInputShouldThrowIllegalArgumentException() {
		BigDecimal negative = BigDecimal.valueOf(-1L);
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Money(negative));
	}

	@Test
	void shouldAddTwoMoneyObjectsCorrectly() {
		Money moneyOne = new Money("10.00");
		Money moneyTwo = new Money("5.50");
		Money expected = new Money("15.50");

		Money result = moneyOne.add(moneyTwo);

		Assertions.assertEquals(expected, result);
	}

	@Test
	void shouldMultiplyMoneyByQuantityCorrectly() {
		Money money = new Money("10.00");
		Quantity quantity = new Quantity(3);
		Money expected = new Money("30.00");

		Money result = money.multiply(quantity);

		Assertions.assertEquals(expected, result);
	}

	@Test
	void shouldDivideMoneyByMoneyCorrectly() {
		Money moneyOne = new Money("10.00");
		Money moneyTwo = new Money("2.00");
		Money expected = new Money("5.00"); // 10.00 / 2.00

		Money result = moneyOne.divide(moneyTwo);

		Assertions.assertEquals(expected, result);
	}

	@Test
	void shouldCompareTwoMoneyObjectsCorrectly() {
		Money moneyOne = new Money("10.00");
		Money moneyTwo = new Money("5.00");
		Money moneyThree = new Money("10.00");

		Assertions.assertTrue(moneyOne.compareTo(moneyTwo) > 0);
		Assertions.assertTrue(moneyTwo.compareTo(moneyOne) < 0);
		Assertions.assertEquals(0, moneyOne.compareTo(moneyThree));
	}

	@Test
	void cloneShouldReturnAnEqualButDifferentObject() {
		Money original = new Money("100.00");
		Money cloned = original.clone();

		Assertions.assertEquals(original, cloned);
		Assertions.assertNotSame(original, cloned);
	}

	@Test
	void zeroConstantShouldHaveValueZero() {
		Assertions.assertEquals(new Money("0.00"), Money.ZERO);
	}

	@Test
	void shouldThrowExceptionWhenValueIsNegativeForConstructorWithString() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Money("-10.00"));
	}

	@Test
	void shouldThrowExceptionWhenValueIsNegativeForConstructorWithBigDecimal() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-10.00")));
	}
}