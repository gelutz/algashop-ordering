package com.lutz.algashop.ordering.infrastructure.persistence.provider;

import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.repository.Orders;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {
	private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;
	private final OrderPersistenceEntityAssembler assembler;
	private final OrderPersistenceEntityDisassembler disassembler;

	@Override
	public Optional<Order> ofId(OrderId orderId) {
		var result = orderPersistenceEntityRepository.findById(orderId.value().toLong())
				.orElseThrow();
		return Optional.of(disassembler.fromPersistence(result));
	}

	@Override
	public boolean exists(OrderId orderId) {
		return false;
	}

	@Override
	public void add(Order aggregateRoot) {
		OrderPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);

		orderPersistenceEntityRepository.saveAndFlush(persistenceEntity);
	}

	@Override
	public int count() {
		return 0;
	}
}
