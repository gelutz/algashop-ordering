package com.lutz.algashop.ordering.domain.entity.builder;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

public class ShoppingCartTestBuilder {

    private ShoppingCartId id = new ShoppingCartId();
    private CustomerId customerId = new CustomerId();
    private Set<ShoppingCartItem> items = new HashSet<>();
    private Money totalAmount = Money.ZERO;
    private Quantity totalItems = Quantity.ZERO;
    private OffsetDateTime createdAt = OffsetDateTime.now();

    private ShoppingCartTestBuilder() {
    }

    public static ShoppingCartTestBuilder aFreshShoppingCart() {
        return new ShoppingCartTestBuilder()
                .withId(new ShoppingCartId())
                .withCustomerId(new CustomerId())
                .withItems(new HashSet<>())
                .withTotalAmount(Money.ZERO)
                .withTotalItems(Quantity.ZERO)
                .withCreatedAt(OffsetDateTime.now());
    }

    public static ShoppingCartTestBuilder anExistingShoppingCart() {
        return new ShoppingCartTestBuilder()
                .withId(new ShoppingCartId())
                .withCustomerId(new CustomerId())
                .withItems(new HashSet<>())
                .withTotalAmount(Money.ZERO)
                .withTotalItems(Quantity.ZERO)
                .withCreatedAt(OffsetDateTime.now());
    }

    public ShoppingCartTestBuilder withId(ShoppingCartId id) {
        this.id = id;
        return this;
    }

    public ShoppingCartTestBuilder withCustomerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public ShoppingCartTestBuilder withItems(Set<ShoppingCartItem> items) {
        this.items = items;
        return this;
    }

    public ShoppingCartTestBuilder withTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public ShoppingCartTestBuilder withTotalItems(Quantity totalItems) {
        this.totalItems = totalItems;
        return this;
    }

    public ShoppingCartTestBuilder withCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ShoppingCart build() {
        if (totalAmount.equals(Money.ZERO) && totalItems.equals(Quantity.ZERO) && items.isEmpty()) {
            return ShoppingCart.startShopping(customerId);
        }

        return ShoppingCart.existing()
                .id(id)
                .customerId(customerId)
                .totalAmount(totalAmount)
                .totalItems(totalItems)
                .createdAt(createdAt)
                .items(items)
                .build();
    }
}
