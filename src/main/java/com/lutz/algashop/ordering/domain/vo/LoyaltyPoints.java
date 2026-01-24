package com.lutz.algashop.ordering.domain.vo;

import lombok.NonNull;

public record LoyaltyPoints(Integer value) implements Comparable<LoyaltyPoints> {
	public static final LoyaltyPoints ZERO = new LoyaltyPoints();

	public LoyaltyPoints() {
		this(0);
	}

	public LoyaltyPoints(@NonNull Integer value) {
		if (value < 0) throw new IllegalArgumentException();

		this.value = value;
	}

	public LoyaltyPoints add(@NonNull Integer value) {
		return add(new LoyaltyPoints(value));
	}

	public LoyaltyPoints add(@NonNull LoyaltyPoints points) {
		return new LoyaltyPoints(this.value() + points.value());
	}

	@Override
	public String toString() {
		return value.toString();
	}


	@Override
	public int compareTo(LoyaltyPoints o) {
		return this.value().compareTo(o.value());
	}
}
