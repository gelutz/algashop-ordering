package com.lutz.algashop.ordering.infrastructure.persistence.repository;

import com.lutz.algashop.ordering.domain.utils.IdGenerator;
import com.lutz.algashop.ordering.infrastructure.persistence.builder.OrderPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
		OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestBuilder
				.existing()
				.id(id)
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