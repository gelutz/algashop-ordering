package com.lutz.algashop.ordering.application.shoppingcart.management;

import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.customer.CustomerNotFoundException;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductCatalogService;
import com.lutz.algashop.ordering.domain.product.ProductNotFoundException;
import com.lutz.algashop.ordering.domain.product.ProductOutOfStockException;
import com.lutz.algashop.ordering.domain.product.builder.ProductTestBuilder;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCarts;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItemId;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartCreatedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartEmptiedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartItemAddedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartItemRemovedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.CustomerAlreadyHasShoppingCartException;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartDoesNotContainItemException;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartNotFoundException;
import com.lutz.algashop.ordering.infrastructure.listener.shoppingcart.ShoppingCartEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ShoppingCartManagementApplicationServiceIT {

	@Autowired
	private ShoppingCartManagementApplicationService sut;

	@Autowired
	private ShoppingCarts shoppingCarts;

	@Autowired
	private Customers customers;

	@MockitoBean
	private ProductCatalogService productCatalogService;

	@MockitoSpyBean
	private ShoppingCartEventListener shoppingCartEventListener;

	@BeforeEach
	void setup() {
		if (!customers.exists(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)) {
			customers.add(CustomerTestBuilder.aCustomer().build());
		}
	}

	@Nested
	class CreateNew {

		@Test
		void shouldCreateNewShoppingCart() {
			UUID cartId = sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

			assertNotNull(cartId);
			assertTrue(shoppingCarts.ofId(new ShoppingCartId(cartId)).isPresent());
		}

		@Test
		void shouldThrowWhenCustomerNotFound() {
			assertThrows(CustomerNotFoundException.class,
					() -> sut.createNew(UUID.randomUUID()));
		}

		@Test
		void shouldThrowWhenCustomerAlreadyHasCart() {
			sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

			assertThrows(CustomerAlreadyHasShoppingCartException.class,
					() -> sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value()));
		}
	}

	@Nested
	class AddItem {

		private UUID cartId;

		@BeforeEach
		void setupCart() {
			cartId = sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());
		}

		@Test
		void shouldAddItemToShoppingCart() {
			Product product = ProductTestBuilder.aProduct().build();
			Mockito.when(productCatalogService.ofId(product.id()))
					.thenReturn(Optional.of(product));

			ShoppingCartItemInput input = ShoppingCartItemInput.builder()
					.shoppingCartId(cartId)
					.productId(product.id().value())
					.quantity(2)
					.build();

			sut.addItem(input);

			ShoppingCart updatedCart = shoppingCarts.ofId(new ShoppingCartId(cartId)).orElseThrow();
			assertEquals(1, updatedCart.items().size());
			assertEquals(2, updatedCart.totalItems().value());
		}

		@Test
		void shouldThrowWhenCartNotFound() {
			Product product = ProductTestBuilder.aProduct().build();
			Mockito.when(productCatalogService.ofId(product.id()))
					.thenReturn(Optional.of(product));

			ShoppingCartItemInput input = ShoppingCartItemInput.builder()
					.shoppingCartId(UUID.randomUUID())
					.productId(product.id().value())
					.quantity(1)
					.build();

			assertThrows(ShoppingCartNotFoundException.class,
					() -> sut.addItem(input));
		}

		@Test
		void shouldThrowWhenProductNotFound() {
			UUID nonExistentProductId = UUID.randomUUID();
			Mockito.when(productCatalogService.ofId(Mockito.any()))
					.thenReturn(Optional.empty());

			ShoppingCartItemInput input = ShoppingCartItemInput.builder()
					.shoppingCartId(cartId)
					.productId(nonExistentProductId)
					.quantity(1)
					.build();

			assertThrows(ProductNotFoundException.class,
					() -> sut.addItem(input));
		}

		@Test
		void shouldThrowWhenProductOutOfStock() {
			Product unavailableProduct = ProductTestBuilder.aProductUnavailable().build();
			Mockito.when(productCatalogService.ofId(unavailableProduct.id()))
					.thenReturn(Optional.of(unavailableProduct));

			ShoppingCartItemInput input = ShoppingCartItemInput.builder()
					.shoppingCartId(cartId)
					.productId(unavailableProduct.id().value())
					.quantity(1)
					.build();

			assertThrows(ProductOutOfStockException.class,
					() -> sut.addItem(input));
		}
	}

	@Nested
	class RemoveItem {

		private UUID cartId;

		@BeforeEach
		void setupCartWithItem() {
			cartId = sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

			Product product = ProductTestBuilder.aProduct().build();
			Mockito.when(productCatalogService.ofId(product.id()))
					.thenReturn(Optional.of(product));

			ShoppingCartItemInput input = ShoppingCartItemInput.builder()
					.shoppingCartId(cartId)
					.productId(product.id().value())
					.quantity(1)
					.build();
			sut.addItem(input);
		}

		@Test
		void shouldRemoveItemFromShoppingCart() {
			ShoppingCart cart = shoppingCarts.ofId(new ShoppingCartId(cartId)).orElseThrow();
			ShoppingCartItemId itemId = cart.items().iterator().next().id();

			sut.removeItem(cartId, itemId.value());

			ShoppingCart updatedCart = shoppingCarts.ofId(new ShoppingCartId(cartId)).orElseThrow();
			assertTrue(updatedCart.items().isEmpty());
		}

		@Test
		void shouldThrowWhenCartNotFound() {
			assertThrows(ShoppingCartNotFoundException.class,
					() -> sut.removeItem(UUID.randomUUID(), UUID.randomUUID()));
		}

		@Test
		void shouldThrowWhenItemNotFound() {
			assertThrows(ShoppingCartDoesNotContainItemException.class,
					() -> sut.removeItem(cartId, UUID.randomUUID()));
		}
	}

	@Nested
	class Empty {

		private UUID cartId;

		@BeforeEach
		void setupCartWithItem() {
			cartId = sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

			Product product = ProductTestBuilder.aProduct().build();
			Mockito.when(productCatalogService.ofId(product.id()))
					.thenReturn(Optional.of(product));

			ShoppingCartItemInput input = ShoppingCartItemInput.builder()
					.shoppingCartId(cartId)
					.productId(product.id().value())
					.quantity(1)
					.build();
			sut.addItem(input);
		}

		@Test
		void shouldEmptyShoppingCart() {
			sut.empty(cartId);

			ShoppingCart updatedCart = shoppingCarts.ofId(new ShoppingCartId(cartId)).orElseThrow();
			assertTrue(updatedCart.isEmpty());
			assertEquals(0, updatedCart.items().size());
		}

		@Test
		void shouldThrowWhenCartNotFound() {
			assertThrows(ShoppingCartNotFoundException.class,
					() -> sut.empty(UUID.randomUUID()));
		}
	}

	@Nested
	class Delete {

		private UUID cartId;

		@BeforeEach
		void setupCart() {
			cartId = sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());
		}

		@Test
		void shouldDeleteShoppingCart() {
			sut.delete(cartId);

			assertTrue(shoppingCarts.ofId(new ShoppingCartId(cartId)).isEmpty());
		}

		@Test
		void shouldThrowWhenCartNotFound() {
			assertThrows(ShoppingCartNotFoundException.class,
					() -> sut.delete(UUID.randomUUID()));
		}
	}

	@Nested
	class DomainEvents {

		@Test
		void shouldEmitShoppingCartCreatedEventWhenCartIsCreated() {
			sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

			Mockito.verify(shoppingCartEventListener, Mockito.times(1))
					.listen(Mockito.any(ShoppingCartCreatedEvent.class));
		}

		@Test
		void shouldEmitShoppingCartEmptiedEventWhenCartIsEmptied() {
			UUID cartId = sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

			Product product = ProductTestBuilder.aProduct().build();
			Mockito.when(productCatalogService.ofId(product.id()))
					.thenReturn(Optional.of(product));

			ShoppingCartItemInput input = ShoppingCartItemInput.builder()
					.shoppingCartId(cartId)
					.productId(product.id().value())
					.quantity(1)
					.build();
			sut.addItem(input);

			sut.empty(cartId);

			Mockito.verify(shoppingCartEventListener, Mockito.times(1))
					.listen(Mockito.any(ShoppingCartEmptiedEvent.class));
		}

		@Test
		void shouldEmitShoppingCartItemAddedEventWhenItemIsAdded() {
			UUID cartId = sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

			Product product = ProductTestBuilder.aProduct().build();
			Mockito.when(productCatalogService.ofId(product.id()))
					.thenReturn(Optional.of(product));

			ShoppingCartItemInput input = ShoppingCartItemInput.builder()
					.shoppingCartId(cartId)
					.productId(product.id().value())
					.quantity(2)
					.build();
			sut.addItem(input);

			Mockito.verify(shoppingCartEventListener, Mockito.times(1))
					.listen(Mockito.any(ShoppingCartItemAddedEvent.class));
		}

		@Test
		void shouldEmitShoppingCartItemRemovedEventWhenItemIsRemoved() {
			UUID cartId = sut.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

			Product product = ProductTestBuilder.aProduct().build();
			Mockito.when(productCatalogService.ofId(product.id()))
					.thenReturn(Optional.of(product));

			ShoppingCartItemInput input = ShoppingCartItemInput.builder()
					.shoppingCartId(cartId)
					.productId(product.id().value())
					.quantity(1)
					.build();
			sut.addItem(input);

			ShoppingCart cart = shoppingCarts.ofId(new ShoppingCartId(cartId)).orElseThrow();
			ShoppingCartItemId itemId = cart.items().iterator().next().id();

			sut.removeItem(cartId, itemId.value());

			Mockito.verify(shoppingCartEventListener, Mockito.times(1))
					.listen(Mockito.any(ShoppingCartItemRemovedEvent.class));
		}
	}
}
