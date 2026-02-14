package com.lutz.algashop.ordering.infrastructure.persistence.provider;

import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.repository.Orders;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrdersPersistenceProvider implements Orders {
	private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;
	private final OrderPersistenceEntityAssembler assembler;
	private final OrderPersistenceEntityDisassembler disassembler;

	private final EntityManager entityManager;

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

		orderPersistenceEntityRepository
				.findById(aggregateRoot.id().value().toLong())
				.ifPresentOrElse(
					entity -> update(aggregateRoot, entity),
                    () -> insert(aggregateRoot)
				);
	}

	private void insert(Order aggregateRoot) {
		OrderPersistenceEntity entity = assembler.fromDomain(aggregateRoot);
		entity = orderPersistenceEntityRepository.saveAndFlush(entity);
		updateVersion(aggregateRoot, entity);
	}

	private void update(Order aggregateRoot, OrderPersistenceEntity entity) {
		OrderPersistenceEntity merged = assembler.merge(entity, aggregateRoot);
		entityManager.detach(merged); // this detaches the object from hibernate, allowing you to change the version
		entity = orderPersistenceEntityRepository.saveAndFlush(merged);
		updateVersion(aggregateRoot, entity);
	}

	@SneakyThrows
	private void updateVersion(Order aggregateRoot, OrderPersistenceEntity entity) {
		Field versionField = aggregateRoot.getClass().getDeclaredField("version");
		versionField.setAccessible(true);
		ReflectionUtils.setField(versionField, aggregateRoot, entity.getVersion());
		versionField.setAccessible(false);
	}

	@Override
	public int count() {
		return 0;
	}
}
