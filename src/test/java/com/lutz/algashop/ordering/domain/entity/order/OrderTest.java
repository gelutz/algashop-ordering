package com.lutz.algashop.ordering.domain.entity.order;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.builder.ProductTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.domain.exception.order.OrderCannotBeEditedException;
import com.lutz.algashop.ordering.domain.exception.order.OrderDoesNotContainOrderItemException;
import com.lutz.algashop.ordering.domain.exception.order.ProductOutOfStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Nested
    @DisplayName("Order.draft() tests")
    class DraftTests {

        private void assertDraftOptionalFieldsAreNull(Order order) {
            assertNull(order.paidAt());
            assertNull(order.placedAt());
            assertNull(order.canceledAt());
            assertNull(order.readyAt());
            assertNull(order.billing());
            assertNull(order.shipping());
            assertNull(order.paymentMethod());
        }

        @Test
        void shouldCreateDraftOrderWithInitialState() {
            CustomerId customerId = new CustomerId();

            Order order = Order.draft(customerId);

            assertNotNull(order);
            assertNotNull(order.id());
            assertEquals(customerId, order.customerId());
            assertEquals(OrderStatus.DRAFT, order.status());
            assertEquals(Money.ZERO, order.totalAmount());
            assertEquals(Quantity.ZERO, order.itemsAmount());
            assertNotNull(order.items());
            assertTrue(order.items().isEmpty());
            assertDraftOptionalFieldsAreNull(order);
        }

        @Test
        void shouldThrowExceptionWhenCustomerIdIsNull() {
            assertThrows(NullPointerException.class, () -> Order.draft(null));
        }
    }

    @Nested
    @DisplayName("Order.existing() builder tests")
    class ExistingBuilderTests {

        private OrderId orderId;
        private CustomerId customerId;
        private Money totalAmount;
        private Quantity itemsAmount;
        private OffsetDateTime paidAt;
        private OffsetDateTime placedAt;
        private OffsetDateTime canceledAt;
        private OffsetDateTime readyAt;
        private Billing billing;
        private Shipping shipping;
        private OrderStatus status;
        private PaymentMethod paymentMethod;
        private Set<OrderItem> items;

        @BeforeEach
        void setUp() {
            orderId = new OrderId();
            customerId = new CustomerId();
            totalAmount = new Money(new BigDecimal("100.00"));
            itemsAmount = new Quantity(5);
            paidAt = OffsetDateTime.now();
            placedAt = OffsetDateTime.now().minusDays(1);
            canceledAt = null;
            readyAt = OffsetDateTime.now().plusDays(1);
            billing = OrderTestBuilder.aBilling().build();
            shipping = OrderTestBuilder.aShipping().build();
            status = OrderStatus.PAID;
            paymentMethod = PaymentMethod.CREDIT_CARD;
            items = new HashSet<>();
        }

        private Order.ExistingOrderBuilder baseBuilder() {
            return Order.existing()
                    .id(orderId)
                    .customerId(customerId)
                    .totalAmount(totalAmount)
                    .itemsAmount(itemsAmount)
                    .paidAt(paidAt)
                    .placedAt(placedAt)
                    .canceledAt(canceledAt)
                    .readyAt(readyAt)
                    .billing(billing)
                    .shipping(shipping)
                    .status(status)
                    .paymentMethod(paymentMethod)
                    .items(items);
        }

        @Test
        void shouldCreateOrderWithAllFieldsCorrectly() {
            Order order = baseBuilder().build();

            assertNotNull(order);
            assertEquals(orderId, order.id());
            assertEquals(customerId, order.customerId());
            assertEquals(totalAmount, order.totalAmount());
            assertEquals(itemsAmount, order.itemsAmount());
            assertEquals(paidAt, order.paidAt());
            assertEquals(placedAt, order.placedAt());
            assertEquals(canceledAt, order.canceledAt());
            assertEquals(readyAt, order.readyAt());
            assertEquals(billing, order.billing());
            assertEquals(shipping, order.shipping());
            assertEquals(status, order.status());
            assertEquals(paymentMethod, order.paymentMethod());
            assertEquals(items, order.items());
        }

        @Test
        void shouldThrowNullPointerExceptionForNotNullableFields() {
            assertThrows(NullPointerException.class, () -> baseBuilder()
                    .id(null)
                    .build());
            assertThrows(NullPointerException.class, () -> baseBuilder()
                    .customerId(null)
                    .build());
            assertThrows(NullPointerException.class, () -> baseBuilder()
                    .totalAmount(null)
                    .build());
            assertThrows(NullPointerException.class, () -> baseBuilder()
                    .itemsAmount(null)
                    .build());
            assertThrows(NullPointerException.class, () -> baseBuilder()
                    .status(null)
                    .build());
            assertThrows(NullPointerException.class, () -> baseBuilder()
                    .items(null)
                    .build());
        }

        @Test
        void shouldAllowNullableFields() {
            Order order = baseBuilder()
                    .paidAt(null)
                    .placedAt(null)
                    .canceledAt(null)
                    .readyAt(null)
                    .billing(null)
                    .shipping(null)
                    .paymentMethod(null)
                    .build();

            assertNotNull(order);
            assertNull(order.paidAt());
            assertNull(order.placedAt());
            assertNull(order.canceledAt());
            assertNull(order.readyAt());
            assertNull(order.billing());
            assertNull(order.shipping());
            assertNull(order.paymentMethod());
        }
    }

    @Nested
    @DisplayName("Order equality tests")
    class EqualityTests {

        @Test
        void ordersWithSameIdShouldBeEqual() {
            OrderId sharedId = new OrderId();

            Order order1 = OrderTestBuilder.anExistingOrder()
                    .withId(sharedId)
                    .withTotalAmount(Money.ZERO)
                    .withItemsAmount(Quantity.ZERO)
                    .withItems(new HashSet<>())
                    .build();
            Order order2 = OrderTestBuilder.anExistingOrder()
                    .withId(sharedId)
                    .withTotalAmount(Money.ZERO)
                    .withItemsAmount(Quantity.ZERO)
                    .withItems(new HashSet<>())
                    .build();

            assertEquals(order1, order2);
        }

        @Test
        void ordersWithDifferentIdsShouldNotBeEqual() {
            Order order1 = OrderTestBuilder.anExistingOrder()
                    .withId(new OrderId())
                    .withTotalAmount(Money.ZERO)
                    .withItemsAmount(Quantity.ZERO)
                    .withItems(new HashSet<>())
                    .build();
            Order order2 = OrderTestBuilder.anExistingOrder()
                    .withId(new OrderId())
                    .withTotalAmount(Money.ZERO)
                    .withItemsAmount(Quantity.ZERO)
                    .withItems(new HashSet<>())
                    .build();

            assertNotEquals(order1, order2);
        }
    }

    @Nested
    @DisplayName("Order items collection tests")
    class ItemsCollectionTests {

        @Test
        void itemsShouldReturnUnmodifiableSet() {
            Order order = OrderTestBuilder.aFilledDraftOrder().build();

            Set<OrderItem> items = order.items();
            OrderItem newItem = OrderItem.newOrderBuilder()
                    .orderId(order.id())
                    .product(ProductTestBuilder.aProduct().build())
                    .quantity(new Quantity(1))
                    .build();

            assertThrows(UnsupportedOperationException.class, () -> items.add(newItem));
            assertThrows(UnsupportedOperationException.class, items::clear);
        }

        @Nested
        @DisplayName("Order#addItem tests")
        class AddItemTests {

            private Order sut;
            private ProductId productId;
            private ProductName productName;

            @BeforeEach
            void setUp() {
                sut = Order.draft(new CustomerId());
                productId = new ProductId();
                productName = new ProductName("Test Product");
            }

            private void addItem(BigDecimal price, int quantity) {
                sut.addItem(
                        new Product(productId, productName, new Money(price), true),
                        new Quantity(quantity)
                           );
            }

            @Test
            void shouldAddItemToOrder() {
                addItem(new BigDecimal("25.00"), 2);
                assertEquals(1, sut.items().size());
            }

            @Test
            void shouldAddMultipleItems() {
                addItem(new BigDecimal("25.00"), 2);
                addItem(new BigDecimal("30.00"), 1);
                addItem(new BigDecimal("15.00"), 3);

                assertEquals(3, sut.items().size());
            }

            @Test
            void shouldCreateItemWithCorrectOrderId() {
                addItem(new BigDecimal("25.00"), 2);

                OrderItem addedItem = sut.items().iterator().next();
                assertEquals(sut.id(), addedItem.orderId());
            }

            @Test
            void shouldThrowProductOutOfStockExceptionWhenProductIsOutOfStock() {
                assertThrows(ProductOutOfStockException.class,
                        () -> sut.addItem(
                                new Product(productId, productName, new Money("10"), false),
                                new Quantity(1))
                            );

            }
        }

        @Nested
        class RemoveItemTests {
            private Order sut;
            private final Product product = OrderTestBuilder.aProduct().build();

            @BeforeEach
            void setup() {
                sut = Order.draft(new CustomerId());
                sut.addItem(product, new Quantity(1));
            }

            @Test
            void givenDraftOrderWithGivenOrderItemShouldRemoveItem() {
                assertTrue(sut.isDraft());
                OrderItemId firstItemId = sut.items().iterator().next().id();
                Quantity quantityBefore = sut.itemsAmount();

                assertDoesNotThrow(() -> sut.removeItem(firstItemId));
                assertEquals(new Quantity(quantityBefore.value() - 1), sut.itemsAmount());
            }

            @Test
            void givenDraftOrderWithoutOrderItemShouldThrowOrderDoesNotContainOrderItemException() {
                assertTrue(sut.isDraft());
                OrderItemId firstItemId = sut.items().iterator().next().id();
                sut.removeItem(firstItemId);

                assertThrows(OrderDoesNotContainOrderItemException.class, () -> sut.removeItem(firstItemId));
            }

            @Test
            void givenNotDraftOrderShouldThrowOrderCannotBeEditedException() {
                assertTrue(sut.isDraft());
                sut.changeShipping(OrderTestBuilder.aShipping().build());
                sut.changeBilling(OrderTestBuilder.aBilling().build());
                sut.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE);
                sut.place();

                assertTrue(sut.isPlaced());
                assertThrows(OrderCannotBeEditedException.class, () -> sut.removeItem(new OrderItemId()));
            }
        }
    }
}
