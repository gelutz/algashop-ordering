package com.lutz.algashop.ordering.infrastructure.persistence.reepository;

import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {
}
