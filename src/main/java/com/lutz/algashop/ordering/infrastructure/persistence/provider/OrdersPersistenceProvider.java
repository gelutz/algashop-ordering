package com.lutz.algashop.ordering.infrastructure.persistence.provider;

import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.repository.Orders;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {
	private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;

	@Override
	public Optional<Order> ofId(OrderId orderId) {
		return Optional.empty();
	}

	@Override
	public boolean exists(OrderId orderId) {
		return false;
	}

	@Override
	public void add(Order aggregateRoot) {
		OrderPersistenceEntity persistenceEntity = OrderPersistenceEntity.builder()
		                                                                      .id(aggregateRoot.id().value().toLong())
		                                                                      .customerId(aggregateRoot.customerId()
		                                                                                               .value())
		                                                                      .status(aggregateRoot.status().toString())
		                                                                      .build();

		orderPersistenceEntityRepository.saveAndFlush(persistenceEntity);
	}

	@Override
	public int count() {
		return 0;
	}
}
