package com.lutz.algashop.ordering.domain.entity;

import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.domain.vo.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
class OrderTest {

    private CustomerId createValidCustomerId() {
        return new CustomerId();
    }

    private OrderId createValidOrderId() {
        return new OrderId();
    }

    private Money createValidMoney() {
        return new Money(new BigDecimal("100.00"));
    }

    private Quantity createValidQuantity() {
        return new Quantity(5);
    }

    private BillingInfo createValidBillingInfo() {
        return BillingInfo.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("12345678901"))
                .phone(new Phone("11987654321"))
                .address(createValidAddress())
                .build();
    }

    private ShippingInfo createValidShippingInfo() {
        return ShippingInfo.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("12345678901"))
                .phone(new Phone("11987654321"))
                .address(createValidAddress())
                .build();
    }

    private Address createValidAddress() {
        return new Address("Street", "123", "Neighborhood", "City", "State", "12345-678", new ZipCode("12345678"));
    }

    private Set<OrderItem> createValidItems() {
        return new HashSet<>();
    }

    private ProductId createValidProductId() {
        return new ProductId();
    }

    private ProductName createValidProductName() {
        return new ProductName("Test Product");
    }

    @Nested
    @DisplayName("Order.draft() tests")
    class DraftTests {

        @Test
        void shouldCreateDraftOrderWithInitialState() {
            CustomerId customerId = createValidCustomerId();

            Order order = Order.draft(customerId);

            assertNotNull(order);
            assertNotNull(order.id());
            assertEquals(customerId, order.customerId());
            assertEquals(OrderStatus.DRAFT, order.status());
            assertEquals(Money.ZERO, order.totalAmount());
            assertEquals(Quantity.ZERO, order.itemsAmount());
            assertNotNull(order.items());
            assertTrue(order.items().isEmpty());
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
            orderId = createValidOrderId();
            customerId = createValidCustomerId();
            totalAmount = createValidMoney();
            itemsAmount = createValidQuantity();
            paidAt = OffsetDateTime.now();
            placedAt = OffsetDateTime.now().minusDays(1);
            canceledAt = null;
            readyAt = OffsetDateTime.now().plusDays(1);
            billingInfo = createValidBillingInfo();
            shippingInfo = createValidShippingInfo();
            status = OrderStatus.PAID;
            paymentMethod = PaymentMethod.CREDIT_CARD;
            shippingCost = new Money(new BigDecimal("15.00"));
            expectedDeliveryDate = LocalDate.now().plusDays(7);
            items = createValidItems();
        }

        @Test
        void shouldCreateOrderWithAllFieldsCorrectly() {
            Order order = Order.existing()
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
                    .items(items)
                    .build();

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
            assertNotNull(order.items());
        }

        @Test
        void shouldThrowExceptionWhenIdIsNull() {
            assertThrows(NullPointerException.class, () -> Order.existing()
                    .id(null)
                    .customerId(customerId)
                    .totalAmount(totalAmount)
                    .itemsAmount(itemsAmount)
                    .status(status)
                    .items(items)
                    .build());
        }

        @Test
        void shouldThrowExceptionWhenCustomerIdIsNull() {
            assertThrows(NullPointerException.class, () -> Order.existing()
                    .id(orderId)
                    .customerId(null)
                    .totalAmount(totalAmount)
                    .itemsAmount(itemsAmount)
                    .status(status)
                    .items(items)
                    .build());
        }

        @Test
        void shouldThrowExceptionWhenTotalAmountIsNull() {
            assertThrows(NullPointerException.class, () -> Order.existing()
                    .id(orderId)
                    .customerId(customerId)
                    .totalAmount(null)
                    .itemsAmount(itemsAmount)
                    .status(status)
                    .items(items)
                    .build());
        }

        @Test
        void shouldThrowExceptionWhenItemsAmountIsNull() {
            assertThrows(NullPointerException.class, () -> Order.existing()
                    .id(orderId)
                    .customerId(customerId)
                    .totalAmount(totalAmount)
                    .itemsAmount(null)
                    .status(status)
                    .items(items)
                    .build());
        }

        @Test
        void shouldThrowExceptionWhenStatusIsNull() {
            assertThrows(NullPointerException.class, () -> Order.existing()
                    .id(orderId)
                    .customerId(customerId)
                    .totalAmount(totalAmount)
                    .itemsAmount(itemsAmount)
                    .status(null)
                    .items(items)
                    .build());
        }

        @Test
        void shouldThrowExceptionWhenItemsIsNull() {
            assertThrows(NullPointerException.class, () -> Order.existing()
                    .id(orderId)
                    .customerId(customerId)
                    .totalAmount(totalAmount)
                    .itemsAmount(itemsAmount)
                    .status(status)
                    .items(null)
                    .build());
        }

        @Test
        void shouldAllowNullableFields() {
            Order order = Order.existing()
                    .id(orderId)
                    .customerId(customerId)
                    .totalAmount(totalAmount)
                    .itemsAmount(itemsAmount)
                    .paidAt(null)
                    .placedAt(null)
                    .canceledAt(null)
                    .readyAt(null)
                    .billingInfo(null)
                    .shippingInfo(null)
                    .status(status)
                    .paymentMethod(null)
                    .shippingCost(null)
                    .expectedDeliveryDate(null)
                    .items(items)
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

        @BeforeEach
        void setUp() {
            sut = Order.draft(createValidCustomerId());
        }

        @Test
        void shouldAddItemToOrder() {
            ProductId productId = createValidProductId();
            ProductName productName = createValidProductName();
            Money price = new Money(new BigDecimal("25.00"));
            Quantity quantity = new Quantity(2);

            sut.addItem(productId, productName, price, quantity);

            assertEquals(1, sut.items().size());
        }

        @Test
        void shouldAddMultipleItems() {
            sut.addItem(createValidProductId(), createValidProductName(), new Money(new BigDecimal("25.00")), new Quantity(2));
            sut.addItem(createValidProductId(), createValidProductName(), new Money(new BigDecimal("30.00")), new Quantity(1));
            sut.addItem(createValidProductId(), createValidProductName(), new Money(new BigDecimal("15.00")), new Quantity(3));

            assertEquals(3, sut.items().size());
        }

        @Test
        void shouldCreateItemWithCorrectOrderId() {
            ProductId productId = createValidProductId();
            ProductName productName = createValidProductName();
            Money price = new Money(new BigDecimal("25.00"));
            Quantity quantity = new Quantity(2);

            sut.addItem(productId, productName, price, quantity);

            OrderItem addedItem = sut.items().iterator().next();
            assertEquals(sut.id(), addedItem.orderId());
        }
    }

    @Nested
    @DisplayName("Order equality tests")
    class EqualityTests {

        @Test
        void ordersWithSameIdShouldBeEqual() {
            OrderId sharedId = createValidOrderId();
            CustomerId customerId = createValidCustomerId();

            Order order1 = Order.existing()
                    .id(sharedId)
                    .customerId(customerId)
                    .totalAmount(Money.ZERO)
                    .itemsAmount(Quantity.ZERO)
                    .status(OrderStatus.DRAFT)
                    .items(createValidItems())
                    .build();

            Order order2 = Order.existing()
                    .id(sharedId)
                    .customerId(createValidCustomerId())
                    .totalAmount(new Money(new BigDecimal("500.00")))
                    .itemsAmount(new Quantity(10))
                    .status(OrderStatus.PAID)
                    .items(createValidItems())
                    .build();

            assertEquals(order1, order2);
        }

        @Test
        void ordersWithDifferentIdsShouldNotBeEqual() {
            CustomerId customerId = createValidCustomerId();

            Order order1 = Order.existing()
                    .id(createValidOrderId())
                    .customerId(customerId)
                    .totalAmount(Money.ZERO)
                    .itemsAmount(Quantity.ZERO)
                    .status(OrderStatus.DRAFT)
                    .items(createValidItems())
                    .build();

            Order order2 = Order.existing()
                    .id(createValidOrderId())
                    .customerId(customerId)
                    .totalAmount(Money.ZERO)
                    .itemsAmount(Quantity.ZERO)
                    .status(OrderStatus.DRAFT)
                    .items(createValidItems())
                    .build();

            assertNotEquals(order1, order2);
        }

        @Test
        void orderShouldNotEqualNull() {
            Order order = Order.draft(createValidCustomerId());

            assertNotEquals(null, order);
            assertFalse(order.equals(null));
        }

        @Test
        void orderShouldNotEqualDifferentType() {
            Order order = Order.draft(createValidCustomerId());

            assertFalse(order.equals("not an order"));
            assertFalse(order.equals(123));
        }

        @Test
        void hashCodeShouldBeConsistentWithEquals() {
            OrderId sharedId = createValidOrderId();

            Order order1 = Order.existing()
                    .id(sharedId)
                    .customerId(createValidCustomerId())
                    .totalAmount(Money.ZERO)
                    .itemsAmount(Quantity.ZERO)
                    .status(OrderStatus.DRAFT)
                    .items(createValidItems())
                    .build();

            Order order2 = Order.existing()
                    .id(sharedId)
                    .customerId(createValidCustomerId())
                    .totalAmount(new Money(new BigDecimal("100.00")))
                    .itemsAmount(new Quantity(5))
                    .status(OrderStatus.PAID)
                    .items(createValidItems())
                    .build();

            assertEquals(order1.hashCode(), order2.hashCode());
        }
    }

    @Nested
    @DisplayName("Order items collection tests")
    class ItemsCollectionTests {

        @Test
        void itemsShouldReturnUnmodifiableSet() {
            Order order = Order.draft(createValidCustomerId());
            order.addItem(createValidProductId(), createValidProductName(), new Money(new BigDecimal("25.00")), new Quantity(2));

            Set<OrderItem> items = order.items();
            OrderItem newItem = OrderItem.newOrderBuilder()
                    .orderId(order.id())
                    .productId(createValidProductId())
                    .productName(createValidProductName())
                    .price(new Money(new BigDecimal("10.00")))
                    .quantity(new Quantity(1))
                    .build();

            assertThrows(UnsupportedOperationException.class, () -> items.add(newItem));
            assertThrows(UnsupportedOperationException.class, () -> items.clear());
        }
    }
}
