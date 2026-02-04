package com.lutz.algashop.ordering.domain.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum OrderStatus {
	DRAFT, PLACED(DRAFT), PAID(PLACED), READY(PAID), CANCELED(DRAFT, PLACED, PAID, READY);

	private List<OrderStatus> previousStatuses;
	OrderStatus(OrderStatus... previousStatuses) {
		this.previousStatuses = Arrays.asList(previousStatuses);
	}

	public boolean canChangeTo(OrderStatus newStatus) {
		OrderStatus cur = this;
		return newStatus.previousStatuses.contains(cur);
	}

	public boolean cannotChangeTo(OrderStatus newStatus) {
		return !canChangeTo(newStatus);
	}

	@Override
	public String toString() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}