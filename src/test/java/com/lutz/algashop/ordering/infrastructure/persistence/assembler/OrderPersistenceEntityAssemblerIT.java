package com.lutz.algashop.ordering.infrastructure.persistence.assembler;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.OrderItem;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.infrastructure.persistence.builder.CustomerPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.builder.OrderPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class OrderPersistenceEntityAssemblerIT {
	@Mock
	private CustomerPersistenceEntityRepository customerRepository;
	private final OrderItemPersistenceEntityAssembler itemAssembler = new OrderItemPersistenceEntityAssembler();
	private OrderPersistenceEntityAssembler sut;

	@BeforeEach
	void setup() {
		sut = new OrderPersistenceEntityAssembler(itemAssembler, customerRepository);
		Mockito.when(customerRepository.getReferenceById(Mockito.any(UUID.class)))
				.then(answer -> {
					UUID uuid = answer.getArgument(0);
					return CustomerPersistenceEntityTestBuilder.existing().id(uuid).build();
				});
	}

	@Test
	void givenDomainObjectShouldConvertToPersistenceObject() {
		Order orderStub = OrderTestBuilder.aFilledDraftOrder().build();

		OrderPersistenceEntity result = sut.fromDomain(orderStub);

		assertEquals(result.getId(), orderStub.id().value().toLong());
		assertEquals(result.getCustomerId(), orderStub.customerId().value());
		assertEquals(result.getTotalAmount(), orderStub.totalAmount().value());
		assertEquals(result.getTotalItems(), orderStub.itemsAmount().value());
		assertEquals(result.getStatus(), orderStub.status().name());
		assertEquals(result.getPaymentMethod(), orderStub.paymentMethod().name());
		assertEquals(result.getPlacedAt(), orderStub.placedAt());
		assertEquals(result.getPaidAt(), orderStub.paidAt());
		assertEquals(result.getReadyAt(), orderStub.readyAt());
		assertEquals(result.getCanceledAt(), orderStub.canceledAt());
	}

	@Test
	void givenPersistenceObjectAndDomainObjectShouldMerge() {
		Order orderStub = OrderTestBuilder.aFilledDraftOrder().build();
		OrderPersistenceEntity base = new OrderPersistenceEntity();

		OrderPersistenceEntity result = sut.merge(base, orderStub);

		assertEquals(result.getId(), orderStub.id().value().toLong());
		assertEquals(result.getCustomerId(), orderStub.customerId().value());
		assertEquals(result.getTotalAmount(), orderStub.totalAmount().value());
		assertEquals(result.getTotalItems(), orderStub.itemsAmount().value());
		assertEquals(result.getStatus(), orderStub.status().name());
		assertEquals(result.getPaymentMethod(), orderStub.paymentMethod().name());
		assertEquals(result.getPlacedAt(), orderStub.placedAt());
		assertEquals(result.getPaidAt(), orderStub.paidAt());
		assertEquals(result.getReadyAt(), orderStub.readyAt());
		assertEquals(result.getCanceledAt(), orderStub.canceledAt());
	}

	@Test
	void givenOrderWithNoItemsShouldRemovePersistedItemsEntities() {
		Order orderCollectionWithoutItems = OrderTestBuilder.aFilledDraftOrder()
		                                                    .withProducts(new HashSet<>())
		                                                    .build();
		OrderPersistenceEntity orderPersistenceWithItems = OrderPersistenceEntityTestBuilder.existing().build();

		Assertions.assertTrue(orderCollectionWithoutItems.items().isEmpty());
		Assertions.assertFalse(orderPersistenceWithItems.getItems().isEmpty());

		sut.merge(orderPersistenceWithItems, orderCollectionWithoutItems);

		Assertions.assertTrue(orderPersistenceWithItems.getItems().isEmpty());
	}

	@Test
	void givenOrderWithItemsShouldAddPersistedItemsEntities() {
		Order orderCollectionWithItems = OrderTestBuilder.aFilledDraftOrder()
		                                                    .build();
		OrderPersistenceEntity orderPersistenceWithoutItems = OrderPersistenceEntityTestBuilder
				.existing()
				.items(new HashSet<>())
				.build();

		Assertions.assertFalse(orderCollectionWithItems.items().isEmpty());
		Assertions.assertTrue(orderPersistenceWithoutItems.getItems().isEmpty());

		sut.merge(orderPersistenceWithoutItems, orderCollectionWithItems);

		Assertions.assertFalse(orderPersistenceWithoutItems.getItems().isEmpty());
	}

	@Test
	void givenOrderWithItemsWhenMergedShouldRemoveItemsNotInTheNewList() {
		ProductId productId = new ProductId();
		Product productInBothLists = OrderTestBuilder.aProduct().id(productId).build();
		Product productToBeRemoved = OrderTestBuilder.aProduct().build();

		Order order = OrderTestBuilder
				.aFilledDraftOrder()
				.withProducts(Set.of(productToBeRemoved, productInBothLists))
		        .build();

		OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestBuilder
				.existing()
				.items(order
						.items()
						.stream()
						.map(itemAssembler::fromDomain)
						.collect(Collectors.toSet()))
				.build();

		Assertions.assertFalse(order.items().isEmpty());
		Assertions.assertFalse(orderPersistenceEntity.getItems().isEmpty());

		// removes an item from the order collection, that needs to be persisted (in this cased removed) in the test.
		OrderItem itemToBeRemoved = order.items()
		                                 .stream()
		                                 .filter(item -> item.productId() == productToBeRemoved.id())
		                                 .findFirst()
				                         .orElseThrow();

		order.removeItem(itemToBeRemoved.id());

		sut.merge(orderPersistenceEntity, order);

		Assertions.assertEquals(1, orderPersistenceEntity.getItems().size());
		OrderItemPersistenceEntity resultItem = orderPersistenceEntity.getItems().iterator().next();
		Assertions.assertEquals(productId.value(), resultItem.getProductId());
	}
}