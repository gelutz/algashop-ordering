package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;


public record Money(BigDecimal value) {
	private static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;
	public static Money ZERO = (new Money("0")).clone();

	public Money {
		if (value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(ErrorMessages.Fields.VALUE_IS_NEGATIVE_OR_ZERO);
		}
		value = value.setScale(2, roundingMode);
	}

	public Money(String value) {
		this(new BigDecimal(value));
	}

	public Money add(Money target) {
		return new Money(this.value().add(target.value()));
	}

	public Money multiply(Quantity target) {
		if (BigDecimal.ONE.compareTo(new BigDecimal(target.value())) > 0) {
			throw new IllegalArgumentException(ErrorMessages.Fields.VALUE_IS_NEGATIVE);
		}
		return new Money(this.value().multiply(new BigDecimal(target.value())));
	}

	public Money divide(Money target) {
		return new Money(this.value().divide(target.value(), roundingMode));
	}

	public int compareTo(Money target) {
		return this.value().compareTo(target.value());
	}


	protected Money clone() {
		return new Money(this.value());
	}

	public @NonNull String toString() {
		return value.toString();
	}
}
