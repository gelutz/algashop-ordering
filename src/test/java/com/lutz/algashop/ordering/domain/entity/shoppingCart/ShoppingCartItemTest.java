package com.lutz.algashop.ordering.domain.entity.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.builder.ProductTestBuilder;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.domain.exception.shoppingCart.ShoppingCartItemIncompatibleProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
@DisplayName("ShoppingCartItem Tests")
class ShoppingCartItemTest {

    private ShoppingCartItemId shoppingCartItemId;
    private ShoppingCartId shoppingCartId;
    private ProductId productId;
    private ProductName productName;
    private Money price;
    private Quantity quantity;
    private Money totalAmount;

    @BeforeEach
    void setUp() {
        shoppingCartItemId = new ShoppingCartItemId();
        shoppingCartId = new ShoppingCartId();
        productId = new ProductId();
        productName = new ProductName("Test Product Name");
        price = new Money(new BigDecimal("10.00"));
        quantity = new Quantity(2);
        totalAmount = new Money(new BigDecimal("20.00"));
    }

    @Nested
    @DisplayName("fresh() tests")
    class FreshTests {

        @Test
        @DisplayName("Should create item with correct total amount")
        void shouldCreateItemWithCorrectTotalAmount() {
            Product product = ProductTestBuilder.aProduct()
                    .id(productId)
                    .productName(productName)
                    .price(price)
                    .inStock(true)
                    .build();

            ShoppingCartItem item = ShoppingCartItem.fresh(shoppingCartId, product, quantity);

            assertNotNull(item);
            assertNotNull(item.id());
            assertEquals(shoppingCartId, item.shoppingCartId());
            assertEquals(productId, item.productId());
            assertEquals(productName, item.name());
            assertEquals(price, item.price());
            assertEquals(quantity, item.quantity());
            assertTrue(item.available());
            assertEquals(price.multiply(quantity), item.totalAmount());
        }

        @Test
        @DisplayName("Should throw exception when required field is null")
        void shouldThrowExceptionWhenRequiredFieldIsNull() {
            Product product = ProductTestBuilder.aProduct().build();

            assertThrows(NullPointerException.class, () -> ShoppingCartItem.fresh(null, product, quantity));
            assertThrows(NullPointerException.class, () -> ShoppingCartItem.fresh(shoppingCartId, null, quantity));
            assertThrows(NullPointerException.class, () -> ShoppingCartItem.fresh(shoppingCartId, product, null));
        }
    }

    @Nested
    @DisplayName("existing() builder tests")
    class ExistingBuilderTests {

        @Test
        @DisplayName("Should create shopping cart item with all fields correctly")
        void shouldCreateShoppingCartItemWithAllFieldsCorrectly() {
            ShoppingCartItem item = ShoppingCartItem.existing()
                    .id(shoppingCartItemId)
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(productName)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build();

            assertNotNull(item);
            assertEquals(shoppingCartItemId, item.id());
            assertEquals(shoppingCartId, item.shoppingCartId());
            assertEquals(productId, item.productId());
            assertEquals(productName, item.name());
            assertEquals(price, item.price());
            assertEquals(quantity, item.quantity());
            assertEquals(totalAmount, item.totalAmount());
            assertTrue(item.available());
        }

        @Test
        @DisplayName("Should throw NullPointerException for not nullable fields")
        void shouldThrowNullPointerExceptionForNotNullableFields() {
            assertThrows(NullPointerException.class, () -> ShoppingCartItem.existing()
                    .id(null)
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(productName)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build());

            assertThrows(NullPointerException.class, () -> ShoppingCartItem.existing()
                    .id(shoppingCartItemId)
                    .shoppingCartId(null)
                    .productId(productId)
                    .name(productName)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build());

            assertThrows(NullPointerException.class, () -> ShoppingCartItem.existing()
                    .id(shoppingCartItemId)
                    .shoppingCartId(shoppingCartId)
                    .productId(null)
                    .name(productName)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build());

            assertThrows(NullPointerException.class, () -> ShoppingCartItem.existing()
                    .id(shoppingCartItemId)
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(null)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build());

            assertThrows(NullPointerException.class, () -> ShoppingCartItem.existing()
                    .id(shoppingCartItemId)
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(productName)
                    .price(null)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build());

            assertThrows(NullPointerException.class, () -> ShoppingCartItem.existing()
                    .id(shoppingCartItemId)
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(productName)
                    .price(price)
                    .quantity(null)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build());

            assertThrows(NullPointerException.class, () -> ShoppingCartItem.existing()
                    .id(shoppingCartItemId)
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(productName)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(null)
                    .available(true)
                    .build());

            assertThrows(NullPointerException.class, () -> ShoppingCartItem.existing()
                    .id(shoppingCartItemId)
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(productName)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(null)
                    .build());
        }
    }

    @Nested
    @DisplayName("changeQuantity tests")
    class ChangeQuantityTests {

        @Test
        @DisplayName("Should change quantity and recalculate total")
        void shouldChangeQuantityAndRecalculateTotal() {
            Product product = ProductTestBuilder.aProduct()
                    .id(productId)
                    .productName(productName)
                    .price(price)
                    .inStock(true)
                    .build();

            ShoppingCartItem item = ShoppingCartItem.fresh(shoppingCartId, product, quantity);
            Money originalTotal = item.totalAmount();

            Quantity newQuantity = new Quantity(5);
            item.changeQuantity(newQuantity);

            assertEquals(newQuantity, item.quantity());
            assertEquals(price.multiply(newQuantity), item.totalAmount());
            assertNotEquals(originalTotal, item.totalAmount());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when quantity is zero or negative")
        void shouldThrowIllegalArgumentExceptionWhenQuantityIsZeroOrNegative() {
            Product product = ProductTestBuilder.aProduct().build();
            ShoppingCartItem item = ShoppingCartItem.fresh(shoppingCartId, product, quantity);

            assertThrows(IllegalArgumentException.class, () -> item.changeQuantity(new Quantity(0)));
            assertThrows(IllegalArgumentException.class, () -> item.changeQuantity(new Quantity(-1)));
        }
    }

    @Nested
    @DisplayName("refresh tests")
    class RefreshTests {

        @Test
        @DisplayName("Should update item with new product data")
        void shouldUpdateItemWithNewProductData() {
            Product originalProduct = ProductTestBuilder.aProduct()
                    .id(productId)
                    .productName(productName)
                    .price(price)
                    .inStock(true)
                    .build();

            ShoppingCartItem item = ShoppingCartItem.fresh(shoppingCartId, originalProduct, quantity);

            ProductName updatedName = new ProductName("Updated Product Name");
            Money updatedPrice = new Money(new BigDecimal("15.00"));
            Product updatedProduct = Product.builder()
                    .id(productId)
                    .productName(updatedName)
                    .price(updatedPrice)
                    .inStock(false)
                    .build();

            item.refresh(updatedProduct);

            assertEquals(updatedName, item.name());
            assertEquals(updatedPrice, item.price());
            assertFalse(item.available());
            assertEquals(updatedPrice.multiply(quantity), item.totalAmount());
        }

        @Test
        @DisplayName("Should throw ShoppingCartItemIncompatibleProductException when product ID mismatch")
        void shouldThrowShoppingCartItemIncompatibleProductExceptionWhenProductIdMismatch() {
            Product originalProduct = ProductTestBuilder.aProduct()
                    .id(productId)
                    .build();

            ShoppingCartItem item = ShoppingCartItem.fresh(shoppingCartId, originalProduct, quantity);

            Product differentProduct = ProductTestBuilder.aProduct()
                    .id(new ProductId())
                    .build();

            assertThrows(ShoppingCartItemIncompatibleProductException.class, () -> item.refresh(differentProduct));
        }
    }

    @Nested
    @DisplayName("Equality tests")
    class EqualityTests {

        @Test
        @DisplayName("Shopping cart items with same ID should be equal")
        void shoppingCartItemsWithSameIdShouldBeEqual() {
            ShoppingCartItemId sameId = new ShoppingCartItemId();

            ShoppingCartItem item1 = ShoppingCartItem.existing()
                    .id(sameId)
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(productName)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build();

            ShoppingCartItem item2 = ShoppingCartItem.existing()
                    .id(sameId)
                    .shoppingCartId(new ShoppingCartId())
                    .productId(new ProductId())
                    .name(new ProductName("Different Name"))
                    .price(new Money(new BigDecimal("99.99")))
                    .quantity(new Quantity(99))
                    .totalAmount(new Money(new BigDecimal("9999.99")))
                    .available(false)
                    .build();

            assertEquals(item1, item2);
            assertEquals(item1.hashCode(), item2.hashCode());
        }

        @Test
        @DisplayName("Shopping cart items with different IDs should not be equal")
        void shoppingCartItemsWithDifferentIdsShouldNotBeEqual() {
            ShoppingCartItem item1 = ShoppingCartItem.existing()
                    .id(new ShoppingCartItemId())
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(productName)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build();

            ShoppingCartItem item2 = ShoppingCartItem.existing()
                    .id(new ShoppingCartItemId())
                    .shoppingCartId(shoppingCartId)
                    .productId(productId)
                    .name(productName)
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .available(true)
                    .build();

            assertNotEquals(item1, item2);
        }
    }
}
