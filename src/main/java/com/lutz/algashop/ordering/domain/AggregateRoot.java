package com.lutz.algashop.ordering.domain;

public interface AggregateRoot<ID> extends DomainEventSource {
	ID id();
}
