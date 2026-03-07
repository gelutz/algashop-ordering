package com.lutz.algashop.ordering.infrastructure.persistence.provider;

import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.entity.builder.ProductTestBuilder;
import com.lutz.algashop.ordering.domain.entity.builder.ShoppingCartTestBuilder;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCart;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartItem;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartItemPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceEntityAssembler;
import com.lutz.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfiguration;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartItemPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.lutz.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
		ShoppingCartUpdateProvider.class,
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShoppingCartUpdateProviderIT {

	private final ShoppingCartsPersistenceProvider persistenceProvider;
	private final CustomersPersistenceProvider customersPersistenceProvider;
	private final ShoppingCartPersistenceEntityRepository entityRepository;

	private final ShoppingCartUpdateProvider shoppingCartUpdateProvider;

	@Autowired
	public ShoppingCartUpdateProviderIT(ShoppingCartsPersistenceProvider persistenceProvider,
	                                    CustomersPersistenceProvider customersPersistenceProvider,
	                                    ShoppingCartPersistenceEntityRepository entityRepository,
	                                    ShoppingCartUpdateProvider shoppingCartUpdateProvider) {
		this.persistenceProvider = persistenceProvider;
		this.customersPersistenceProvider = customersPersistenceProvider;
		this.entityRepository = entityRepository;
		this.shoppingCartUpdateProvider = shoppingCartUpdateProvider;
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
	@Transactional(propagation = Propagation.NEVER)
	void shouldUpdateItemPriceAndTotalAmount() {
		ShoppingCart shoppingCart = ShoppingCartTestBuilder.fresh().withItems(new HashSet<>()).build();

		Product product1 = ProductTestBuilder.aProduct().price(new Money("2000")).build();
		Product product2 = ProductTestBuilder.aProduct().id(new ProductId()).price(new Money("200")).build();

		shoppingCart.addItem(product1, new Quantity(2));
		shoppingCart.addItem(product2, new Quantity(1));

		persistenceProvider.add(shoppingCart);

		ProductId productIdToUpdate = product1.id();
		Money newProduct1Price = new Money("1500");
		Money expectedNewItemTotalPrice = newProduct1Price.multiply(new Quantity(2));
		Money expectedNewCartTotalAmount = expectedNewItemTotalPrice.add(new Money("200"));

		shoppingCartUpdateProvider.adjustPrice(productIdToUpdate, newProduct1Price);

		ShoppingCart updatedShoppingCart = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();

		assertEquals(expectedNewCartTotalAmount, updatedShoppingCart.totalAmount());
		assertEquals(new Quantity(3), updatedShoppingCart.totalItems());

		ShoppingCartItem item = updatedShoppingCart.findItem(productIdToUpdate);

		assertEquals(expectedNewItemTotalPrice, item.totalAmount());
		assertEquals(newProduct1Price, item.price());

	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	@Transactional(propagation = Propagation.NEVER)
	void shouldUpdateItemAvailability() {
		ShoppingCart shoppingCart = ShoppingCartTestBuilder.fresh().withItems(new HashSet<>()).build();

		Product product1 = ProductTestBuilder.aProduct()
		                                         .price(new Money("2000"))
		                                         .inStock(true).build();
		Product product2 = ProductTestBuilder.aProduct()
		                                         .id(new ProductId())
		                                         .price(new Money("200"))
		                                         .inStock(true).build();

		shoppingCart.addItem(product1, new Quantity(2));
		shoppingCart.addItem(product2, new Quantity(1));

		persistenceProvider.add(shoppingCart);

		var productIdToUpdate = product1.id();
		var productIdNotToUpdate = product2.id();

		shoppingCartUpdateProvider.changeAvailability(productIdToUpdate, false);

		ShoppingCart updatedShoppingCart = persistenceProvider.ofId(shoppingCart.id()).orElseThrow();

		ShoppingCartItem item = updatedShoppingCart.findItem(productIdToUpdate);

		assertFalse(item.available());

		ShoppingCartItem item2 = updatedShoppingCart.findItem(productIdNotToUpdate);

		assertTrue(item2.available());

	}


}