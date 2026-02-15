package com.lutz.algashop.ordering.domain.repository;

import com.lutz.algashop.ordering.domain.entity.AggregateRoot;

import java.util.Optional;

public interface Repository<T extends AggregateRoot<ID>, ID> {

	Optional<T> ofId(ID id);
	boolean exists(ID id);
	void add(T aggregateRoot);
	Long count();
}
