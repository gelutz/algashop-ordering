package com.lutz.algashop.ordering.infrastructure.persistence.provider;

import com.lutz.algashop.ordering.domain.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCarts;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
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
public class ShoppingCartsPersistenceProvider implements ShoppingCarts {
	private final ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository;
	private final ShoppingCartPersistenceEntityAssembler assembler;
	private final ShoppingCartPersistenceEntityDisassembler disassembler;

	private final EntityManager entityManager;

	@Override
	public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
		var result = shoppingCartPersistenceEntityRepository.findById(shoppingCartId.value())
				.orElseThrow();
		return Optional.of(disassembler.fromPersistence(result));
	}

	@Override
	public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
		return shoppingCartPersistenceEntityRepository.findByCustomerId(customerId.value())
				.map(disassembler::fromPersistence);
	}

	@Override
	public boolean exists(ShoppingCartId shoppingCartId) {
		return shoppingCartPersistenceEntityRepository.existsById(shoppingCartId.value());
	}

	@Override
	@Transactional(readOnly = false)
	public void add(ShoppingCart aggregateRoot) {
		shoppingCartPersistenceEntityRepository
				.findById(aggregateRoot.id().value())
				.ifPresentOrElse(
						entity -> update(aggregateRoot, entity),
						() -> insert(aggregateRoot)
				);
	}

	@Override
	@Transactional(readOnly = false)
	public void remove(ShoppingCart aggregateRoot) {
		shoppingCartPersistenceEntityRepository.deleteById(aggregateRoot.id().value());
	}

	@Override
	@Transactional(readOnly = false)
	public void remove(ShoppingCartId id) {
		shoppingCartPersistenceEntityRepository.deleteById(id.value());
	}

	@Override
	public Long count() {
		return shoppingCartPersistenceEntityRepository.count();
	}

	private void insert(ShoppingCart aggregateRoot) {
		ShoppingCartPersistenceEntity entity = assembler.fromDomain(aggregateRoot);
		entity = shoppingCartPersistenceEntityRepository.saveAndFlush(entity);
		updateVersion(aggregateRoot, entity);
	}

	private void update(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity entity) {
		ShoppingCartPersistenceEntity merged = assembler.merge(entity, aggregateRoot);
		entityManager.detach(merged);
		entity = shoppingCartPersistenceEntityRepository.saveAndFlush(merged);
		updateVersion(aggregateRoot, entity);
	}

	@SneakyThrows
	private void updateVersion(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity entity) {
		Field versionField = aggregateRoot.getClass().getDeclaredField("version");
		versionField.setAccessible(true);
		ReflectionUtils.setField(versionField, aggregateRoot, entity.getVersion());
		versionField.setAccessible(false);
	}
}
