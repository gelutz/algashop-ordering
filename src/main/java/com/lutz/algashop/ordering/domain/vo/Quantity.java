package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;

import java.util.Objects;

public record Quantity(Integer value) implements Comparable<Quantity> {
	public static Quantity ZERO = (new Quantity(0)).clone();

	public Quantity {
		Objects.requireNonNull(value, ErrorMessages.Fields.VALUE_IS_NULL);
		if (value < 0) {
			throw new IllegalArgumentException(ErrorMessages.Fields.VALUE_IS_NEGATIVE);
		}
	}


	protected Quantity clone() {
		return new Quantity(this.value());
	}

	@Override
	public int compareTo(Quantity o) {
		return this.value().compareTo(o.value());
	}
}
