package com.lutz.algashop.ordering.domain.entity.builder;

import com.lutz.algashop.ordering.domain.entity.Order;
import com.lutz.algashop.ordering.domain.entity.OrderItem;
import com.lutz.algashop.ordering.domain.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.domain.vo.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

public class OrderTestBuilder {

    private OrderId id = new OrderId();
    private CustomerId customerId = new CustomerId();
    private Money totalAmount = new Money(new BigDecimal("100.00"));
    private Quantity itemsAmount = new Quantity(5);
    private OffsetDateTime paidAt;
    private OffsetDateTime placedAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;
    private BillingInfo billingInfo;
    private ShippingInfo shippingInfo;
    private OrderStatus status = OrderStatus.DRAFT;
    private PaymentMethod paymentMethod;
    private Money shippingCost;
    private LocalDate expectedDeliveryDate;
    private Set<OrderItem> items = new HashSet<>();

    private OrderTestBuilder() {
    }

    public static OrderTestBuilder aDraftOrder() {
        return new OrderTestBuilder();
    }

    public static OrderTestBuilder anExistingOrder() {
        OrderTestBuilder builder = new OrderTestBuilder();
        builder.id = new OrderId();
        builder.status = OrderStatus.PLACED;
        builder.billingInfo = createDefaultBillingInfo();
        builder.shippingInfo = createDefaultShippingInfo();
        return builder;
    }

    private static BillingInfo createDefaultBillingInfo() {
        return BillingInfo.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("12345678901"))
                .phone(new Phone("11987654321"))
                .address(createDefaultAddress())
                .build();
    }

    private static ShippingInfo createDefaultShippingInfo() {
        return ShippingInfo.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("12345678901"))
                .phone(new Phone("11987654321"))
                .address(createDefaultAddress())
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

    public OrderTestBuilder withPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
        return this;
    }

    public OrderTestBuilder withPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
        return this;
    }

    public OrderTestBuilder withCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
        return this;
    }

    public OrderTestBuilder withReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
        return this;
    }

    public OrderTestBuilder withBillingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
        return this;
    }

    public OrderTestBuilder withShippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
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

    public OrderTestBuilder withShippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
        return this;
    }

    public OrderTestBuilder withExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        return this;
    }

    public OrderTestBuilder withItems(Set<OrderItem> items) {
        this.items = items;
        return this;
    }

    public OrderTestBuilder with() {
        return this;
    }

    public Order build() {
        if (status == OrderStatus.DRAFT && paidAt == null && placedAt == null && canceledAt == null && readyAt == null && billingInfo == null && shippingInfo == null && paymentMethod == null && shippingCost == null && expectedDeliveryDate == null && items.isEmpty()) {
            // Match the Order.draft factory logic if it looks like a draft
             return Order.draft(customerId);
        }

        return Order.existing()
                .id(id)
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
    }
}
