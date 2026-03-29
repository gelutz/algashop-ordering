package com.lutz.algashop.ordering.domain;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractEventSourceEntity implements DomainEventSource {
	protected final List<Object> domainEvents = new ArrayList<>();

	protected void publishDomainEvent(@NonNull Object event) {
		this.domainEvents.add(event);
	}

	public List<Object> domainEvents() {
		return Collections.unmodifiableList(this.domainEvents);
	}

	public void clearDomainEvents() {

	}
}
