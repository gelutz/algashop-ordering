package com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart;

import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.shoppingCart.builder.ShoppingCartTestBuilder;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfiguration;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomerPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.customers.CustomersPersistenceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
		ShoppingCartsPersistenceProvider.class,
		ShoppingCartPersistenceEntityAssembler.class,
		ShoppingCartPersistenceEntityDisassembler.class,
		ShoppingCartItemPersistenceEntityAssembler.class,
		ShoppingCartItemPersistenceEntityDisassembler.class,
		CustomersPersistenceProvider.class,
		CustomerPersistenceEntityAssembler.class,
		CustomerPersistenceEntityDisassembler.class,
		SpringDataAuditingConfiguration.class
})
class ShoppingCartsPersistenceProviderIT {

	private final ShoppingCartsPersistenceProvider persistenceProvider;
	private final CustomersPersistenceProvider customersPersistenceProvider;
	private final ShoppingCartPersistenceEntityRepository entityRepository;

	@Autowired
	public ShoppingCartsPersistenceProviderIT(ShoppingCartsPersistenceProvider persistenceProvider,
	                                          CustomersPersistenceProvider customersPersistenceProvider,
	                                          ShoppingCartPersistenceEntityRepository entityRepository) {
		this.persistenceProvider = persistenceProvider;
		this.customersPersistenceProvider = customersPersistenceProvider;
		this.entityRepository = entityRepository;
	}

	@BeforeEach
	public void setup() {
		if (!customersPersistenceProvider.exists(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)) {
			customersPersistenceProvider.add(
					CustomerTestBuilder.aCustomer().build()
			                                );
		}
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldAddAndFindShoppingCart() {
		ShoppingCart shoppingCart = ShoppingCartTestBuilder.existing()
				                                           .build();
		assertNull(shoppingCart.version());

		persistenceProvider.add(shoppingCart);

		assertNotNull(shoppingCart.version());
		assertEquals(0L, shoppingCart.version());

		ShoppingCart foundCart = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();
		assertNotNull(foundCart);
		assertEquals(shoppingCart.id(), foundCart.id());
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldRemoveShoppingCartById() {
		ShoppingCart shoppingCart = ShoppingCartTestBuilder.fresh().build();
		persistenceProvider.add(shoppingCart);
		assertTrue(persistenceProvider.exists(shoppingCart.id()));

		persistenceProvider.remove(shoppingCart.id());

		assertFalse(persistenceProvider.exists(shoppingCart.id()));
		assertTrue(entityRepository.findById(shoppingCart.id().value()).isEmpty());
	}

	@Test
	public void shouldRemoveShoppingCartByEntity() {
		ShoppingCart shoppingCart = ShoppingCartTestBuilder.fresh().build();
		persistenceProvider.add(shoppingCart);
		assertTrue(persistenceProvider.exists(shoppingCart.id()));

		persistenceProvider.remove(shoppingCart);

		assertFalse(persistenceProvider.exists(shoppingCart.id()));
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void shouldFindShoppingCartByCustomerId() {
		ShoppingCart shoppingCart = ShoppingCartTestBuilder.fresh()
		                                                       .withCustomerId(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)
		                                                       .build();
		persistenceProvider.add(shoppingCart);

		ShoppingCart foundCart = persistenceProvider.ofCustomer(CustomerTestBuilder.DEFAULT_CUSTOMER_ID).orElseThrow();

		assertNotNull(foundCart);
		assertEquals(CustomerTestBuilder.DEFAULT_CUSTOMER_ID, foundCart.customerId());
		assertEquals(shoppingCart.id(), foundCart.id());
	}

	@Test
	public void shouldCorrectlyCountShoppingCarts() {
		long initialCount = persistenceProvider.count();

		ShoppingCart cart1 = ShoppingCartTestBuilder.fresh().build();
		persistenceProvider.add(cart1);

		Customer otherCustomer = CustomerTestBuilder.aCustomer().withId(new CustomerId()).build();
		customersPersistenceProvider.add(otherCustomer);

		ShoppingCart cart2 = ShoppingCartTestBuilder.fresh().withCustomerId(otherCustomer.id()).build();
		persistenceProvider.add(cart2);

		long finalCount = persistenceProvider.count();

		assertEquals(initialCount + 2, finalCount);
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void shouldAddAndFindWhenNoTransaction() {
		ShoppingCart shoppingCart = ShoppingCartTestBuilder.fresh().build();

		persistenceProvider.add(shoppingCart);

		assertDoesNotThrow(() -> {
			ShoppingCart foundCart = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();
			assertNotNull(foundCart);
		});
	}
}