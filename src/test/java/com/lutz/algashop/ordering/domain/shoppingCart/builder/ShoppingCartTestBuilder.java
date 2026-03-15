package com.lutz.algashop.ordering.domain.shoppingCart.builder;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItem;

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

    public static ShoppingCartTestBuilder fresh() {
        return new ShoppingCartTestBuilder()
                .withId(new ShoppingCartId())
                .withCustomerId(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)
                .withItems(new HashSet<>())
                .withTotalAmount(Money.ZERO)
                .withTotalItems(Quantity.ZERO)
                .withCreatedAt(OffsetDateTime.now());
    }

    public static ShoppingCartTestBuilder existing() {
        return new ShoppingCartTestBuilder()
                .withId(new ShoppingCartId())
                .withCustomerId(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)
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
