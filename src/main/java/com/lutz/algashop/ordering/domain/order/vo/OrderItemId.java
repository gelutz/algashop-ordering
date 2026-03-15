package com.lutz.algashop.ordering.domain.order.vo;

import com.lutz.algashop.ordering.domain.IdGenerator;
import io.hypersistence.tsid.TSID;
import lombok.NonNull;

public record OrderItemId(@NonNull TSID value) {

	public OrderItemId() {
		this(IdGenerator.generateTSID());
	}

	public OrderItemId(Long value) {
		this(TSID.from(value));
	}

	public OrderItemId(String value) {
		this(TSID.from(value));
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
