package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.IdGenerator;
import lombok.NonNull;

import java.util.UUID;

public record CustomerId(@NonNull UUID value) {
	public CustomerId() {
		this(IdGenerator.generateTimeBasedUUID());
	}


	@Override
	public String toString() {
		return value.toString();
	}
}
