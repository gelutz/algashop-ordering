package com.lutz.algashop.ordering.domain.entity.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.builder.ProductTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.domain.exception.order.ProductOutOfStockException;
import com.lutz.algashop.ordering.domain.exception.shoppingCart.ShoppingCartDoesNotContainItemException;
import org.junit.jupiter.api.*;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("DataFlowIssue")
class ShoppingCartTest {
	@DisplayName("ShoppingCart#startShopping and ShoppingCart#existing builders")
	@Nested
	class ShoppingCartBuilderTests {
		@Test
		void startShoppingShouldStartWithDefaultValues() {
			ShoppingCart sut = ShoppingCart.startShopping(new CustomerId());

			Money expectedTotalAmount = Money.ZERO;
			Quantity expectedTotalItems = Quantity.ZERO;

			Assertions.assertEquals(expectedTotalAmount, sut.totalAmount());
			Assertions.assertEquals(expectedTotalItems, sut.totalItems());
			Assertions.assertTrue(sut.createdAt().isBefore(OffsetDateTime.now()));
		}

		@Test
		void existingShouldThrowNullPointerExceptionIfAnyFieldIsNull() {
			ShoppingCartId id = new ShoppingCartId();
			CustomerId customerId = new CustomerId();
			Money totalAmount = new Money("10");
			Quantity totalItems = new Quantity(1);
			OffsetDateTime createdAt = OffsetDateTime.now();

			ShoppingCart.ExistingShoppingCartBuilder sut = ShoppingCart.existing()
			                               .id(id)
			                               .customerId(customerId)
			                               .totalAmount(totalAmount)
			                               .totalItems(totalItems)
			                               .createdAt(createdAt);

			Assertions.assertThrows(NullPointerException.class, () -> sut.id(null).build());
			Assertions.assertThrows(NullPointerException.class, () -> sut.customerId(null).build());
			Assertions.assertThrows(NullPointerException.class, () -> sut.totalAmount(null).build());
			Assertions.assertThrows(NullPointerException.class, () -> sut.totalItems(null).build());
			Assertions.assertThrows(NullPointerException.class, () -> sut.createdAt(null).build());
		}
	}

	@Nested
	@DisplayName("Equality tests")
	class EqualityTests {

		@Test
		@DisplayName("Shopping cart items with same ID should be equal")
		void shoppingCartItemsWithSameIdShouldBeEqual() {
			ShoppingCart item1 = ShoppingCart.startShopping(new CustomerId());

			ShoppingCart item2 = ShoppingCart.existing()
			                                         .id(item1.id())
					                                 .customerId(item1.customerId())
					                                 .totalAmount(item1.totalAmount())
					                                 .totalItems(item1.totalItems())
					                                 .createdAt(item1.createdAt())
							                         .items(item1.items())
							                         .build();

			assertEquals(item1, item2);
			assertEquals(item1.hashCode(), item2.hashCode());
		}

		@Test
		@DisplayName("Shopping cart items with different IDs should not be equal")
		void shoppingCartItemsWithDifferentIdsShouldNotBeEqual() {
			ShoppingCart item1 = ShoppingCart.startShopping(new CustomerId());

			ShoppingCart item2 = ShoppingCart.startShopping(new CustomerId());

			Assertions.assertNotEquals(item1, item2);
			Assertions.assertNotEquals(item1.hashCode(), item2.hashCode());
		}
	}

	@Nested
	@DisplayName("startShopping tests")
	class StartShoppingTests {

		@Test
		@DisplayName("Should create cart with zero totals and empty state")
		void shouldCreateCartWithZeroTotalsAndEmptyState() {
			ShoppingCart sut = ShoppingCart.startShopping(new CustomerId());

			Assertions.assertEquals(Money.ZERO, sut.totalAmount());
			Assertions.assertEquals(Quantity.ZERO, sut.totalItems());
			Assertions.assertTrue(sut.isEmpty());
			Assertions.assertTrue(sut.items().isEmpty());
		}

		@Test
		@DisplayName("Should throw NullPointerException when customerId is null")
		void shouldThrowNullPointerExceptionWhenCustomerIdIsNull() {
			Assertions.assertThrows(NullPointerException.class, () -> ShoppingCart.startShopping(null));
		}
	}

	@Nested
	@DisplayName("addItem tests")
	class AddItemTests {

		private ShoppingCart sut;

		@BeforeEach
		void setUp() {
			sut = ShoppingCart.startShopping(new CustomerId());
		}

		@Test
		@DisplayName("Should throw ProductOutOfStockException when product is out of stock")
		void shouldThrowProductOutOfStockExceptionWhenProductIsOutOfStock() {
			Product outOfStockProduct = ProductTestBuilder.aProduct()
					.inStock(false)
					.build();

			Assertions.assertThrows(ProductOutOfStockException.class,
					() -> sut.addItem(outOfStockProduct, new Quantity(1)));
		}

		@Test
		@DisplayName("Should sum quantities when adding same product twice")
		void shouldSumQuantitiesWhenAddingSameProductTwice() {
			ProductId productId = new ProductId();
			Product product = ProductTestBuilder.aProduct()
					.id(productId)
					.price(new Money("10.00"))
					.build();

			sut.addItem(product, new Quantity(2));
			sut.addItem(product, new Quantity(3));

			Assertions.assertEquals(1, sut.items().size());
			Assertions.assertEquals(new Quantity(5), sut.totalItems());
			Assertions.assertEquals(new Money("50.00"), sut.totalAmount());
		}

		@Test
		@DisplayName("Should add distinct items for different products")
		void shouldAddDistinctItemsForDifferentProducts() {
			Product product1 = ProductTestBuilder.aProduct()
					.price(new Money("10.00"))
					.build();
			Product product2 = ProductTestBuilder.aProduct()
					.price(new Money("20.00"))
					.build();

			sut.addItem(product1, new Quantity(2));
			sut.addItem(product2, new Quantity(1));

			Assertions.assertEquals(2, sut.items().size());
			Assertions.assertEquals(new Quantity(3), sut.totalItems());
			Assertions.assertEquals(new Money("40.00"), sut.totalAmount());
		}
	}

	@Nested
	@DisplayName("removeItem tests")
	class RemoveItemTests {

		private ShoppingCart sut;
		private ShoppingCartItemId existingItemId;

		@BeforeEach
		void setUp() {
			sut = ShoppingCart.startShopping(new CustomerId());
			Product product = ProductTestBuilder.aProduct().build();
			sut.addItem(product, new Quantity(1));
			existingItemId = sut.items().iterator().next().id();
		}

		@Test
		@DisplayName("Should throw ShoppingCartDoesNotContainItemException for non-existent item")
		void shouldThrowShoppingCartDoesNotContainItemExceptionForNonExistentItem() {
			ShoppingCartItemId nonExistentId = new ShoppingCartItemId();

			Assertions.assertThrows(ShoppingCartDoesNotContainItemException.class,
					() -> sut.removeItem(nonExistentId));
		}

		@Test
		@DisplayName("Should remove item and recalculate totals")
		void shouldRemoveItemAndRecalculateTotals() {
			sut.removeItem(existingItemId);

			Assertions.assertTrue(sut.items().isEmpty());
			Assertions.assertEquals(Quantity.ZERO, sut.totalItems());
			Assertions.assertEquals(Money.ZERO, sut.totalAmount());
			Assertions.assertTrue(sut.isEmpty());
		}
	}

	@Nested
	@DisplayName("empty tests")
	class EmptyTests {

		private ShoppingCart sut;

		@BeforeEach
		void setUp() {
			sut = ShoppingCart.startShopping(new CustomerId());
			Product product1 = ProductTestBuilder.aProduct().build();
			Product product2 = ProductTestBuilder.aProduct().build();
			sut.addItem(product1, new Quantity(2));
			sut.addItem(product2, new Quantity(3));
		}

		@Test
		@DisplayName("Should remove all items and reset totals")
		void shouldRemoveAllItemsAndResetTotals() {
			sut.empty();

			Assertions.assertTrue(sut.items().isEmpty());
			Assertions.assertEquals(Quantity.ZERO, sut.totalItems());
			Assertions.assertEquals(Money.ZERO, sut.totalAmount());
			Assertions.assertTrue(sut.isEmpty());
		}
	}

	@Nested
	@DisplayName("refreshItem tests")
	class RefreshItemTests {

		private ShoppingCart sut;
		private ProductId existingProductId;

		@BeforeEach
		void setUp() {
			sut = ShoppingCart.startShopping(new CustomerId());
			existingProductId = new ProductId();
			Product product = ProductTestBuilder.aProduct()
					.id(existingProductId)
					.price(new Money("10.00"))
					.inStock(true)
					.build();
			sut.addItem(product, new Quantity(2));
		}

		@Test
		@DisplayName("Should throw ShoppingCartDoesNotContainProduct when product not in cart")
		void shouldThrowShoppingCartDoesNotContainProductWhenProductNotInCart() {
			Product differentProduct = ProductTestBuilder.aProduct()
					.id(new ProductId())
					.build();

			Assertions.assertThrows(com.lutz.algashop.ordering.domain.exception.shoppingCart.ShoppingCartDoesNotContainProduct.class,
					() -> sut.refreshItem(differentProduct));
		}

		@Test
		@DisplayName("Should update item and recalculate totals when product matches")
		void shouldUpdateItemAndRecalculateTotalsWhenProductMatches() {
			Product updatedProduct = Product.builder()
					.id(existingProductId)
					.productName(new ProductName("Updated Name"))
					.price(new Money("15.00"))
					.inStock(false)
					.build();

			sut.refreshItem(updatedProduct);

			ShoppingCartItem refreshedItem = sut.items().iterator().next();
			Assertions.assertEquals(new ProductName("Updated Name"), refreshedItem.name());
			Assertions.assertEquals(new Money("15.00"), refreshedItem.price());
			Assertions.assertFalse(refreshedItem.available());
			Assertions.assertEquals(new Money("30.00"), sut.totalAmount());
		}
	}

	@Nested
	@DisplayName("items collection tests")
	class ItemsCollectionTests {

		@Test
		@DisplayName("Should return unmodifiable set")
		void shouldReturnUnmodifiableSet() {
			ShoppingCart sut = ShoppingCart.startShopping(new CustomerId());
			Product product = ProductTestBuilder.aProduct().build();
			sut.addItem(product, new Quantity(1));

			Set<ShoppingCartItem> items = sut.items();
			ShoppingCartItem newItem = ShoppingCartItem.fresh(
					new ShoppingCartId(),
					ProductTestBuilder.aProduct().build(),
					new Quantity(1)
			);

			Assertions.assertThrows(UnsupportedOperationException.class, () -> items.add(newItem));
			Assertions.assertThrows(UnsupportedOperationException.class, items::clear);
		}
	}
}