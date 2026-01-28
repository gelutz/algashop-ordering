package com.lutz.algashop.ordering.domain.entity.customer.vo;

import com.lutz.algashop.ordering.domain.utils.IdGenerator;
import lombok.NonNull;

import java.util.UUID;

public record CustomerId(UUID value) {
	public CustomerId() {
		this(IdGenerator.generateTimeBasedUUID());
	}

	public CustomerId(@NonNull UUID value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
