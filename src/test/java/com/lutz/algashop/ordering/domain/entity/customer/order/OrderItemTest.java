package com.lutz.algashop.ordering.domain.entity.customer.order;

import com.lutz.algashop.ordering.domain.entity.order.OrderItem;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    private OrderItemId orderItemId;
    private OrderId orderId;
    private ProductId productId;
    private ProductName productName;
    private Money price;
    private Quantity quantity;
    private Money totalAmount;

    @BeforeEach
    void setUp() {
        orderItemId = new OrderItemId();
        orderId = new OrderId();
        productId = new ProductId();
        productName = new ProductName("Test Product Name");
        price = new Money(new BigDecimal("10.00"));
        quantity = new Quantity(2);
        totalAmount = new Money(new BigDecimal("20.00"));
    }

    @Test
    void existingBuilderShouldCreateOrderItemCorrectly() {
        OrderItem orderItem = OrderItem.existing()
                                       .id(orderItemId)
                                       .orderId(orderId)
                                       .productId(productId)
                                       .productName(productName)
                                       .price(price)
                                       .quantity(quantity)
                                       .totalAmount(totalAmount)
                                       .build();

        assertNotNull(orderItem);
        assertEquals(orderItemId, orderItem.id());
        assertEquals(orderId, orderItem.orderId());
        assertEquals(productId, orderItem.productId());
        assertEquals(productName, orderItem.productName());
        assertEquals(price, orderItem.price());
        assertEquals(quantity, orderItem.quantity());
        assertEquals(totalAmount, orderItem.totalAmount());
    }

    @Test
    void newOrderBuilderShouldCreateOrderItemWithGeneratedIdAndZeroTotalAmount() {
        OrderItem orderItem = OrderItem.newOrderBuilder()
                .orderId(orderId)
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .build();

        assertNotNull(orderItem);
        assertNotNull(orderItem.id()); // Should be generated
        assertEquals(orderId, orderItem.orderId());
        assertEquals(productId, orderItem.productId());
        assertEquals(productName, orderItem.productName());
        assertEquals(price, orderItem.price());
        assertEquals(quantity, orderItem.quantity());
        assertEquals(orderItem.price().multiply(orderItem.quantity()), orderItem.totalAmount()); // Should be the product of price * quantity
    }

    // Test @NonNull validations for newOrderBuilder static method
    @Test
    void newOrderBuilderShouldThrowExceptionWhenRequiredFieldIsNull() {
        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(null)
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .build());

        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(orderId)
                .productId(null)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .build());

        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(orderId)
                .productId(productId)
                .productName(null)
                .price(price)
                .quantity(quantity)
                .build());

        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(orderId)
                .productId(productId)
                .productName(productName)
                .price(null)
                .quantity(quantity)
                .build());

        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(orderId)
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(null)
                .build());
    }
}
