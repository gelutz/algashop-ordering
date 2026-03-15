package com.lutz.algashop.ordering.infrastructure.persistence.customers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerPersistenceEntityRepository extends JpaRepository<CustomerPersistenceEntity, UUID> {
	Optional<CustomerPersistenceEntity> findByEmail(String string);
	boolean existsByEmailAndIdNot(String email, UUID customerId);

}
