package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.utils.IdGenerator;
import io.hypersistence.tsid.TSID;
import lombok.NonNull;

public record OrderId(@NonNull TSID value) {

	public OrderId() {
		this(IdGenerator.generateTSID());
	}

	public OrderId(Long value) {
		this(TSID.from(value));
	}

	public OrderId(String value) {
		this(TSID.from(value));
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
