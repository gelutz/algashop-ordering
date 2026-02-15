package com.lutz.algashop.ordering.domain.entity.builder;

import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.OrderItem;
import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderTestBuilder {

    private OrderId id = new OrderId();
    private CustomerId customerId = new CustomerId();
    private Money totalAmount = new Money("100");
    private Quantity itemsAmount = new Quantity(5);
    private Billing billing;
    private Shipping shipping;
    private OrderStatus status = OrderStatus.DRAFT;
    private PaymentMethod paymentMethod;
    private Set<OrderItem> items = new HashSet<>();

    private OrderTestBuilder() {
    }

    public static OrderTestBuilder aFilledDraftOrder() {
        return new OrderTestBuilder()
                .withId(new OrderId())
                .withStatus(OrderStatus.DRAFT)
                .withBilling(aBilling().build())
                .withShipping(aShipping().build())
                .withPaymentMethod(PaymentMethod.GATEWAY_BALANCE)
                .withProducts(Set.of(OrderTestBuilder.aProduct().build()));
    }

    public static OrderTestBuilder anExistingOrder() {
        return new OrderTestBuilder()
                .withId(new OrderId())
                .withStatus(OrderStatus.PLACED)
                .withPaymentMethod(PaymentMethod.GATEWAY_BALANCE)
                .withBilling(aBilling().build())
                .withShipping(aShipping().build());
    }

    public static Shipping.ShippingBuilder aShipping() {
        return Shipping.builder()
                       .recipient(aRecipient())
                       .address(createDefaultAddress())
                       .cost(new Money("10"))
                       .expectedDeliveryDate(LocalDate.now().plusDays(10));
    }

    public static Billing.BillingBuilder aBilling() {
        return Billing.builder()
                      .fullName(new FullName("John", "Doe"))
                      .document(new Document("12345678901"))
                      .phone(new Phone("11987654321"))
                      .email(new Email("teste@teste.com"))
                      .address(createDefaultAddress());
    }

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                      .id(new ProductId())
                      .productName(new ProductName("Test product"))
                      .price(new Money("50"))
                      .inStock(true);
    }

    public static Recipient aRecipient() {
        return Recipient.builder()
                        .fullName(new FullName("John", "Doe"))
                        .document(new Document("123123"))
                        .phone(new Phone("11123"))
                        .build();
    }

    private static Address createDefaultAddress() {
        return new Address("Street", "123", "Neighborhood", "City", "State", "12345-678", new ZipCode("12345678"));
    }

    public OrderTestBuilder withId(OrderId id) {
        this.id = id;
        return this;
    }

    public OrderTestBuilder withCustomerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestBuilder withTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public OrderTestBuilder withItemsAmount(Quantity itemsAmount) {
        this.itemsAmount = itemsAmount;
        return this;
    }

    public OrderTestBuilder withBilling(Billing billing) {
        this.billing = billing;
        return this;
    }

    public OrderTestBuilder withShipping(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderTestBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderTestBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestBuilder withProducts(Set<Product> products) {
        this.items = products.stream().map(p ->
                OrderItem.newOrderBuilder()
                    .orderId(this.id)
                        .product(p)
                        .quantity(new Quantity(1))
                     .build()).collect(Collectors.toSet());
        return this;
    }

    public OrderTestBuilder withItems(Set<OrderItem> items) {
        this.items = items;
        return this;
    }

    public Order build() {
        if (status == OrderStatus.DRAFT && billing == null && shipping == null && paymentMethod == null && items.isEmpty()) {
            // Match the Order.draft factory logic if it looks like a draft
             return Order.draft(customerId);
        }

        return Order.existing()
                .id(id)
                .customerId(customerId)
                .totalAmount(totalAmount)
                .itemsAmount(itemsAmount)
                .billing(billing)
                .shipping(shipping)
                .status(status)
                .paymentMethod(paymentMethod)
                .items(items)
                .build();
    }
}
