package com.lutz.algashop.ordering.domain.entity.builder;

import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.*;

public class ShoppingCartItemTestBuilder {

    private ShoppingCartItemId id = new ShoppingCartItemId();
    private ShoppingCartId shoppingCartId = new ShoppingCartId();
    private ProductId productId = new ProductId();
    private ProductName name = new ProductName("Test product");
    private Money price = new Money("50");
    private Quantity quantity = new Quantity(1);
    private Money totalAmount = new Money("50");
    private Boolean available = true;

    private ShoppingCartItemTestBuilder() {
    }

    public static ShoppingCartItemTestBuilder aFreshItem() {
        return new ShoppingCartItemTestBuilder();
    }

    public static ShoppingCartItemTestBuilder anExistingItem() {
        return new ShoppingCartItemTestBuilder();
    }

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                      .id(new ProductId())
                      .productName(new ProductName("Test product"))
                      .price(new Money("50"))
                      .inStock(true);
    }

    public ShoppingCartItemTestBuilder withId(ShoppingCartItemId id) {
        this.id = id;
        return this;
    }

    public ShoppingCartItemTestBuilder withShoppingCartId(ShoppingCartId shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
        return this;
    }

    public ShoppingCartItemTestBuilder withProductId(ProductId productId) {
        this.productId = productId;
        return this;
    }

    public ShoppingCartItemTestBuilder withName(ProductName name) {
        this.name = name;
        return this;
    }

    public ShoppingCartItemTestBuilder withPrice(Money price) {
        this.price = price;
        return this;
    }

    public ShoppingCartItemTestBuilder withQuantity(Quantity quantity) {
        this.quantity = quantity;
        return this;
    }

    public ShoppingCartItemTestBuilder withTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public ShoppingCartItemTestBuilder withAvailable(Boolean available) {
        this.available = available;
        return this;
    }

    public ShoppingCartItem build() {
        return ShoppingCartItem.existing()
                .id(id)
                .shoppingCartId(shoppingCartId)
                .productId(productId)
                .name(name)
                .price(price)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .available(available)
                .build();
    }
}
