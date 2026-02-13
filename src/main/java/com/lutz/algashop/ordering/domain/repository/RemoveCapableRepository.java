package com.lutz.algashop.ordering.domain.repository;

import com.lutz.algashop.ordering.domain.entity.AggregateRoot;

public interface RemoveCapableRepository<T extends AggregateRoot<ID>, ID> extends Repository<T, ID> {
	void remove(T t);
	void remove(ID id);
}
