package com.lutz.algashop.ordering.infrastructure.persistence.repository;

import com.lutz.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartPersistenceEntityRepository extends JpaRepository<ShoppingCartPersistenceEntity, UUID> {
	@Query("SELECT s FROM ShoppingCartPersistenceEntity s WHERE s.customer.id = :customerId")
	Optional<ShoppingCartPersistenceEntity> findByCustomerId(UUID customerId);
}
