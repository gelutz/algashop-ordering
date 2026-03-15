package com.lutz.algashop.ordering.infrastructure.persistence.customers;

import com.lutz.algashop.ordering.domain.commons.Email;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
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
	private final CustomerPersistenceEntityRepository persistenceRepository;
	private final CustomerPersistenceEntityAssembler assembler;
	private final CustomerPersistenceEntityDisassembler disassembler;

	private final EntityManager entityManager;

	public Optional<Customer> ofEmail(Email email) {
		return persistenceRepository
				.findByEmail(email.toString())
				.map(disassembler::fromPersistence);
	}

	@Override
	public boolean isEmailUnique(Email email, CustomerId exception) {
		return !persistenceRepository.existsByEmailAndIdNot(email.toString(), exception.value());
	}

	@Override
	public Optional<Customer> ofId(CustomerId customerId) {
		return persistenceRepository
				.findById(customerId.value())
				.map(disassembler::fromPersistence);
	}

	@Override
	public boolean exists(CustomerId customerId) {
		return persistenceRepository.existsById(customerId.value());
	}

	@Override
	@Transactional(readOnly = false)
	public void add(Customer aggregateRoot) {
		persistenceRepository
				.findById(aggregateRoot.id().value())
				.ifPresentOrElse(
					entity ->
							update(aggregateRoot, entity),
						() -> insert(aggregateRoot)
				);
	}

	private void insert(Customer aggregateRoot) {
		CustomerPersistenceEntity entity = assembler.fromDomain(aggregateRoot);
		entity = persistenceRepository.saveAndFlush(entity);
		updateVersion(aggregateRoot, entity);
	}

	private void update(Customer aggregateRoot, CustomerPersistenceEntity entity) {
		CustomerPersistenceEntity merged = assembler.merge(entity, aggregateRoot);
		entityManager.detach(merged);
		entity = persistenceRepository.saveAndFlush(merged);
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
		return persistenceRepository.count();
	}
}
