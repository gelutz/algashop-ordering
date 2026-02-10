package com.lutz.algashop.ordering.domain.entity.customer.vo;

import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record Birthdate(LocalDate date) {
	public Birthdate {
		Objects.requireNonNull(date, ErrorMessages.Fields.BIRTHDATE_IS_NULL);

		if (date.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException(ErrorMessages.Fields.BIRTHDATE_IS_IN_FUTURE);
		}
	}

	public long age() {
		return age(LocalDate.now());
	}

	public long age(LocalDate reference) {
		return ChronoUnit.YEARS.between(date(), reference);
	}

	@Override
	public String toString() {
		return date().toString();
	}
}
