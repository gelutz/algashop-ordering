package com.lutz.algashop.ordering.infrastructure.persistence.provider;

import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.repository.Customers;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomersPersistenceProvider implements Customers {
	private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;
	private final CustomerPersistenceEntityAssembler assembler;
	private final CustomerPersistenceEntityDisassembler disassembler;

	private final EntityManager entityManager;

	@Override
	public Optional<Customer> ofId(CustomerId customerId) {
		var result = customerPersistenceEntityRepository.findById(customerId.value())
				.orElseThrow();
		return Optional.of(disassembler.fromPersistence(result));
	}

	@Override
	public boolean exists(CustomerId customerId) {
		return customerPersistenceEntityRepository.existsById(customerId.value());
	}

	@Override
	@Transactional(readOnly = false)
	public void add(Customer aggregateRoot) {
		customerPersistenceEntityRepository
				.findById(aggregateRoot.id().value())
				.ifPresentOrElse(
					entity ->
							update(aggregateRoot, entity),
						() -> insert(aggregateRoot)
				);
	}

	private void insert(Customer aggregateRoot) {
		CustomerPersistenceEntity entity = assembler.fromDomain(aggregateRoot);
		entity = customerPersistenceEntityRepository.saveAndFlush(entity);
		updateVersion(aggregateRoot, entity);
	}

	private void update(Customer aggregateRoot, CustomerPersistenceEntity entity) {
		CustomerPersistenceEntity merged = assembler.merge(entity, aggregateRoot);
		entityManager.detach(merged);
		entity = customerPersistenceEntityRepository.saveAndFlush(merged);
		updateVersion(aggregateRoot, entity);
	}

	@SneakyThrows
	private void updateVersion(Customer aggregateRoot, CustomerPersistenceEntity entity) {
		Field versionField = aggregateRoot.getClass().getDeclaredField("version");
		versionField.setAccessible(true);
		ReflectionUtils.setField(versionField, aggregateRoot, entity.getVersion());
		versionField.setAccessible(false);
	}

	@Override
	public Long count() {
		return customerPersistenceEntityRepository.count();
	}
}
