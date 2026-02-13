package com.lutz.algashop.ordering.infrastructure.persistence.reepository;

import com.lutz.algashop.ordering.domain.utils.IdGenerator;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // faz o @DataJpaTest usar o banco que configuramos no application-test.yaml
class OrderPersistenceEntityRepositoryIT {

	private final OrderPersistenceEntityRepository sut;


	@Autowired
	public OrderPersistenceEntityRepositoryIT(OrderPersistenceEntityRepository sut) {
		this.sut = sut;
	}

	@Test
	void shouldPersist() {
		Long id = IdGenerator.generateTSID().toLong();
		OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntity.builder()
		                                                     .id(id)
		                                                     .customerId(IdGenerator.generateTimeBasedUUID())
		                                                     .totalItems(2)
		                                                     .totalAmount(new BigDecimal((100)))
		                                                     .status("DRAFT")
		                                                     .paymentMethod("CREDIT_CARD")
		                                                     .placedAt(OffsetDateTime.now())
		                                                     .build();

		sut.saveAndFlush(orderPersistenceEntity);
		Assertions.assertTrue(sut.existsById(id));
	}

	@Test
	public void shouldCount() {
		long ordersCount = sut.count();
		assertEquals(0, ordersCount);
	}
}