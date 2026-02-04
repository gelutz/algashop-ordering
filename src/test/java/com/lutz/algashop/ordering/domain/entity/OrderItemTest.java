package com.lutz.algashop.ordering.domain.entity;

import com.lutz.algashop.ordering.domain.vo.Money;
import com.lutz.algashop.ordering.domain.vo.ProductId;
import com.lutz.algashop.ordering.domain.vo.ProductName;
import com.lutz.algashop.ordering.domain.vo.Quantity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        orderItemId = new OrderItemId(UUID.randomUUID());
        orderId = new OrderId(UUID.randomUUID());
        productId = new ProductId(UUID.randomUUID());
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
        assertEquals(Money.ZERO, orderItem.totalAmount()); // Should be ZERO
    }

    @Test
    void settersShouldUpdateFieldsCorrectly() {
        OrderItem orderItem = OrderItem.existing()
                .id(orderItemId)
                .orderId(orderId)
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .build();

        OrderItemId newOrderItemId = new OrderItemId(UUID.randomUUID());
        OrderId newOrderId = new OrderId(UUID.randomUUID());
        ProductId newProductId = new ProductId(UUID.randomUUID());
        ProductName newProductName = new ProductName("New Product Name");
        Money newPrice = new Money(new BigDecimal("15.00"));
        Quantity newQuantity = new Quantity(3);
        Money newTotalAmount = new Money(new BigDecimal("45.00"));

        orderItem.setId(newOrderItemId);
        orderItem.setOrderId(newOrderId);
        orderItem.setProductId(newProductId);
        orderItem.setProductName(newProductName);
        orderItem.setPrice(newPrice);
        orderItem.setQuantity(newQuantity);
        orderItem.setTotalAmount(newTotalAmount);

        assertEquals(newOrderItemId, orderItem.id());
        assertEquals(newOrderId, orderItem.orderId());
        assertEquals(newProductId, orderItem.productId());
        assertEquals(newProductName, orderItem.productName());
        assertEquals(newPrice, orderItem.price());
        assertEquals(newQuantity, orderItem.quantity());
        assertEquals(newTotalAmount, orderItem.totalAmount());
    }

    // Test @NonNull validations for setters
    @Test
    void setIdShouldThrowExceptionWhenNull() {
        OrderItem orderItem = OrderItem.existing().id(orderItemId).orderId(orderId).productId(productId).productName(productName).price(price).quantity(quantity).totalAmount(totalAmount).build();
        assertThrows(NullPointerException.class, () -> orderItem.setId(null));
    }

    @Test
    void setOrderIdShouldThrowExceptionWhenNull() {
        OrderItem orderItem = OrderItem.existing().id(orderItemId).orderId(orderId).productId(productId).productName(productName).price(price).quantity(quantity).totalAmount(totalAmount).build();
        assertThrows(NullPointerException.class, () -> orderItem.setOrderId(null));
    }

    @Test
    void setProductIdShouldThrowExceptionWhenNull() {
        OrderItem orderItem = OrderItem.existing().id(orderItemId).orderId(orderId).productId(productId).productName(productName).price(price).quantity(quantity).totalAmount(totalAmount).build();
        assertThrows(NullPointerException.class, () -> orderItem.setProductId(null));
    }

    @Test
    void setProductNameShouldThrowExceptionWhenNull() {
        OrderItem orderItem = OrderItem.existing().id(orderItemId).orderId(orderId).productId(productId).productName(productName).price(price).quantity(quantity).totalAmount(totalAmount).build();
        assertThrows(NullPointerException.class, () -> orderItem.setProductName(null));
    }

    @Test
    void setPriceShouldThrowExceptionWhenNull() {
        OrderItem orderItem = OrderItem.existing().id(orderItemId).orderId(orderId).productId(productId).productName(productName).price(price).quantity(quantity).totalAmount(totalAmount).build();
        assertThrows(NullPointerException.class, () -> orderItem.setPrice(null));
    }

    @Test
    void setQuantityShouldThrowExceptionWhenNull() {
        OrderItem orderItem = OrderItem.existing().id(orderItemId).orderId(orderId).productId(productId).productName(productName).price(price).quantity(quantity).totalAmount(totalAmount).build();
        assertThrows(NullPointerException.class, () -> orderItem.setQuantity(null));
    }

    @Test
    void setTotalAmountShouldThrowExceptionWhenNull() {
        OrderItem orderItem = OrderItem.existing().id(orderItemId).orderId(orderId).productId(productId).productName(productName).price(price).quantity(quantity).totalAmount(totalAmount).build();
        assertThrows(NullPointerException.class, () -> orderItem.setTotalAmount(null));
    }

    // Test @NonNull validations for newOrderBuilder static method
    @Test
    void newOrderBuilderShouldThrowExceptionWhenOrderIdIsNull() {
        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(null)
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .build());
    }

    @Test
    void newOrderBuilderShouldThrowExceptionWhenProductIdIsNull() {
        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(orderId)
                .productId(null)
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .build());
    }

    @Test
    void newOrderBuilderShouldThrowExceptionWhenProductNameIsNull() {
        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(orderId)
                .productId(productId)
                .productName(null)
                .price(price)
                .quantity(quantity)
                .build());
    }

    @Test
    void newOrderBuilderShouldThrowExceptionWhenPriceIsNull() {
        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(orderId)
                .productId(productId)
                .productName(productName)
                .price(null)
                .quantity(quantity)
                .build());
    }

    @Test
    void newOrderBuilderShouldThrowExceptionWhenQuantityIsNull() {
        assertThrows(NullPointerException.class, () -> OrderItem.newOrderBuilder()
                .orderId(orderId)
                .productId(productId)
                .productName(productName)
                .price(price)
                .quantity(null)
                .build());
    }
}
