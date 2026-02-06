package com.lutz.algashop.ordering.domain.entity.customer.order;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.builder.ProductTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.OrderItem;
import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import com.lutz.algashop.ordering.domain.exception.InvalidShippingDeliveryDateException;
import com.lutz.algashop.ordering.domain.exception.ProductOutOfStockException;
import com.lutz.algashop.ordering.domain.exception.order.OrderDoesNotContainOrderItemException;
import com.lutz.algashop.ordering.domain.exception.order.OrderStatusCannotBeChangedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
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
            assertNull(order.billingInfo());
            assertNull(order.shippingInfo());
            assertNull(order.paymentMethod());
            assertNull(order.shippingCost());
            assertNull(order.expectedDeliveryDate());
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
        private BillingInfo billingInfo;
        private ShippingInfo shippingInfo;
        private OrderStatus status;
        private PaymentMethod paymentMethod;
        private Money shippingCost;
        private LocalDate expectedDeliveryDate;
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
            billingInfo = BillingInfo.builder()
                    .fullName(new FullName("John", "Doe"))
                    .document(new Document("12345678901"))
                    .phone(new Phone("11987654321"))
                    .address(new Address("Street", "123", "Neighborhood", "City", "State", "12345-678", new ZipCode("12345678")))
                    .build();
            shippingInfo = ShippingInfo.builder()
                    .fullName(new FullName("John", "Doe"))
                    .document(new Document("12345678901"))
                    .phone(new Phone("11987654321"))
                    .address(new Address("Street", "123", "Neighborhood", "City", "State", "12345-678", new ZipCode("12345678")))
                    .build();
            status = OrderStatus.PAID;
            paymentMethod = PaymentMethod.CREDIT_CARD;
            shippingCost = new Money(new BigDecimal("15.00"));
            expectedDeliveryDate = LocalDate.now().plusDays(7);
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
                    .billingInfo(billingInfo)
                    .shippingInfo(shippingInfo)
                    .status(status)
                    .paymentMethod(paymentMethod)
                    .shippingCost(shippingCost)
                    .expectedDeliveryDate(expectedDeliveryDate)
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
            assertEquals(billingInfo, order.billingInfo());
            assertEquals(shippingInfo, order.shippingInfo());
            assertEquals(status, order.status());
            assertEquals(paymentMethod, order.paymentMethod());
            assertEquals(shippingCost, order.shippingCost());
            assertEquals(expectedDeliveryDate, order.expectedDeliveryDate());
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
                    .billingInfo(null)
                    .shippingInfo(null)
                    .paymentMethod(null)
                    .shippingCost(null)
                    .expectedDeliveryDate(null)
                    .build();

            assertNotNull(order);
            assertNull(order.paidAt());
            assertNull(order.placedAt());
            assertNull(order.canceledAt());
            assertNull(order.readyAt());
            assertNull(order.billingInfo());
            assertNull(order.shippingInfo());
            assertNull(order.paymentMethod());
            assertNull(order.shippingCost());
            assertNull(order.expectedDeliveryDate());
        }
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
    }

    @Nested
    @DisplayName("Order status tests")
    class OrderStatusTests {

        @Test
        void givenDraftOrderPlaceShouldSetOrderAsPlaced() {
            Order sut = OrderTestBuilder.aFilledDraftOrder().build();
            sut.place();

            assertTrue(sut.isPlaced());
        }

        @Test
        void givenPlacedOrderPlaceShouldThrowOrderStatusCannotBeChangedException() {
            Order sut = OrderTestBuilder.aFilledDraftOrder().build();
            sut.place();

            OrderStatusCannotBeChangedException exception =
                    assertThrows(OrderStatusCannotBeChangedException.class, sut::place);

            assertEquals(exception.getMessage(), ErrorMessages.Orders.orderStatusCannotBeChanged(sut.id(), OrderStatus.PLACED, OrderStatus.PLACED));
        }
    }
    @Nested
    @DisplayName("Order#changeShippingInfo tests")
    class ChangeShippingInfoTests {
        private Order sut;
        private ShippingInfo shippingInfo;
        private Money shippingCost;

        @BeforeEach
        void setUp() {
            sut = OrderTestBuilder.anExistingOrder()
                    .withStatus(OrderStatus.DRAFT)
                    .withShippingInfo(null)
                    .withShippingCost(null)
                    .withExpectedDeliveryDate(null)
                    .build();
            shippingInfo = createDefaultShippingInfo();
            shippingCost = new Money(new BigDecimal("15.00"));
        }

        private ShippingInfo createDefaultShippingInfo() {
            return ShippingInfo.builder()
                    .fullName(new FullName("John", "Doe"))
                    .document(new Document("12345678901"))
                    .phone(new Phone("11987654321"))
                    .address(new Address("Street", "123", "Neighborhood", "City", "State", "12345-678", new ZipCode("12345678")))
                    .build();
        }

        @Test
        @DisplayName("Should update shipping info with valid future delivery date")
        void shouldUpdateShippingInfoWithValidFutureDeliveryDate() {
            LocalDate futureDate = LocalDate.now().plusDays(7);

            sut.changeShippingInfo(shippingInfo, shippingCost, futureDate);

            assertEquals(shippingInfo, sut.shippingInfo());
            assertEquals(shippingCost, sut.shippingCost());
            assertEquals(futureDate, sut.expectedDeliveryDate());
        }

        @Test
        @DisplayName("Should throw InvalidShippingDeliveryDateException when date is in the past")
        void shouldThrowInvalidShippingDeliveryDateExceptionWhenDateIsInThePast() {
            LocalDate pastDate = LocalDate.now().minusDays(1);

            InvalidShippingDeliveryDateException exception =
                    assertThrows(InvalidShippingDeliveryDateException.class, () -> {
                        sut.changeShippingInfo(shippingInfo, shippingCost, pastDate);
                    });

            assertEquals(
                    ErrorMessages.Orders.orderExpectedDeliveryDateIsInvalid(sut.id(), pastDate),
                    exception.getMessage()
            );
        }
    }

    @Nested
    @DisplayName("Order#changeItemQuantity tests")
    class ChangeItemQuantityTests {
        private Order sut;
        private OrderItem existingItem;

        @BeforeEach
        void setUp() {
            OrderItemId itemId = new OrderItemId();
            Money price = new Money(new BigDecimal("25.00"));
            Quantity quantity = new Quantity(2);
            Money totalAmount = new Money(price.value().multiply(BigDecimal.valueOf(quantity.value())));
            
            existingItem = OrderItem.existing()
                    .id(itemId)
                    .orderId(new OrderId())
                    .productId(new ProductId())
                    .productName(new ProductName("Test Product"))
                    .price(price)
                    .quantity(quantity)
                    .totalAmount(totalAmount)
                    .build();

            Set<OrderItem> items = new HashSet<>();
            items.add(existingItem);

            sut = OrderTestBuilder.anExistingOrder()
                    .withStatus(OrderStatus.DRAFT)
                    .withItems(items)
                    .withTotalAmount(totalAmount)
                    .withItemsAmount(quantity)
                    .build();
        }

        @Test
        @DisplayName("Should throw OrderDoesNotContainOrderItemException when item not found")
        void shouldThrowExceptionWhenItemNotFound() {
            OrderItemId nonExistentItemId = new OrderItemId();

            OrderDoesNotContainOrderItemException exception =
                    assertThrows(OrderDoesNotContainOrderItemException.class, () -> {
                        sut.changeItemQuantity(nonExistentItemId, new Quantity(5));
                    });

            assertEquals(
                    ErrorMessages.Orders.orderDoesNotContainOrderItem(sut.id(), nonExistentItemId),
                    exception.getMessage()
            );
        }

        @Test
        @DisplayName("Should change quantity of existing item")
        void shouldChangeQuantityOfExistingItem() {
            Quantity newQuantity = new Quantity(5);

            sut.changeItemQuantity(existingItem.id(), newQuantity);

            OrderItem updatedItem = sut.items().stream()
                    .filter(i -> i.id().equals(existingItem.id()))
                    .findFirst()
                    .orElseThrow();
            assertEquals(newQuantity, updatedItem.quantity());
        }

        @Test
        @DisplayName("Should recalculate totals after changing item quantity")
        void shouldRecalculateTotalsAfterChangingItemQuantity() {
            Money initialTotal = sut.totalAmount();
            Quantity initialItemsAmount = sut.itemsAmount();

            Quantity newQuantity = new Quantity(10);
            sut.changeItemQuantity(existingItem.id(), newQuantity);

            assertNotEquals(initialTotal, sut.totalAmount());
            assertNotEquals(initialItemsAmount, sut.itemsAmount());

            BigDecimal expectedItemTotal = existingItem.price().value()
                    .multiply(BigDecimal.valueOf(newQuantity.value()));
            assertTrue(sut.totalAmount().value().compareTo(expectedItemTotal) >= 0);
            assertEquals(newQuantity, sut.itemsAmount());
        }
    }
}
