package com.lutz.algashop.ordering.domain.entity.order.vo;

import com.lutz.algashop.ordering.domain.utils.IdGenerator;
import lombok.NonNull;

import java.util.UUID;

public record ProductId(UUID value) {
	public ProductId() {
		this(IdGenerator.generateTimeBasedUUID());
	}

	public ProductId(@NonNull UUID value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
