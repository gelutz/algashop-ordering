package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record Birthdate(LocalDate date) {
	public Birthdate {
		Objects.requireNonNull(date, ErrorMessages.Validation.BIRTHDATE_MUST_NOT_BE_NULL);

		if (date.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException(ErrorMessages.Validation.BIRTHDATE_MUST_IN_PAST);
		}
	}

	public long age() {
		return ChronoUnit.YEARS.between(date(),LocalDate.now());
	}

	@Override
	public String toString() {
		return date().toString();
	}
}
