package com.lutz.algashop.ordering.domain;

public interface RemoveCapableRepository<T extends AggregateRoot<ID>, ID> extends Repository<T, ID> {
	void remove(T t);
	void remove(ID id);
}
