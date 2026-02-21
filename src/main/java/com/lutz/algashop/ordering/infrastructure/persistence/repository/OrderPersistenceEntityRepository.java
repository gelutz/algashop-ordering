package com.lutz.algashop.ordering.infrastructure.persistence.repository;

import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {
	@Query("""
		SELECT o
			FROM OrderPersistenceEntity o
			WHERE o.customer.id = :customerId AND YEAR(o.placedAt) = :year
		""")
	List<OrderPersistenceEntity> placedByCustomerInYear(
			@Param("customerId") UUID customerId,
			@Param("year") Integer year
	);

	@Query("""
		SELECT count(o)
			FROM OrderPersistenceEntity o
			WHERE o.customer.id = :customerId
			AND YEAR(o.placedAt) = :year
			and o.paidAt is not null
			and o.canceledAt is null
		""")
	long salesQuantityByCustomerInYear(
			@Param("customerId") UUID customerId,
			@Param("year") Integer year
	                                                   );

	@Query("""
		SELECT coalesce(sum(o.totalAmount), 0)
			FROM OrderPersistenceEntity o
			WHERE o.customer.id = :customerId
			and o.canceledAt is null
			and o.paidAt is not null
		""")
	BigDecimal totalSolForCustomer(@Param("customerId") UUID customerId);
}
