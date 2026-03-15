As expected, `aProductUnavailable()` doesn't exist yet — we'll need to add it to `ProductTestBuilder`. Now I have everything I need for the plan.

---

## Plan: BuyNowService Implementation

Here's the comprehensive plan for this challenge:

### Overview

We need to create a `BuyNowService` domain service with a `buyNow` method and a corresponding `BuyNowServiceTest` test class. This is a pure domain service (no repository or external dependencies), similar to `CustomerLoyaltyPointsService`.

### Key Observations from Codebase Analysis

- The challenge says `product.checkOutOfStock()` but the actual method is `product.verifyIfIsInStock()` — we'll use the correct method name
- `Quantity(0)` is valid (it doesn't throw), but `Money.multiply(Quantity(0))` throws `IllegalArgumentException` because `Money.multiply` rejects values < 1 (line 30-31 of `Money.java`). This is what drives the "invalid quantity" test case
- `ProductTestBuilder` does **not** have `aProductUnavailable()` — we need to add it
- Shipping has a default cost of `new Money("10")` in `OrderTestBuilder.aShipping()`
- Product default price is `new Money("25.00")` in `ProductTestBuilder` / `new Money("50")` in `OrderTestBuilder`

### Files to Create/Modify

#### 1. Git Worktree Setup
- Create a new branch `buy-now-service` from `main`
- Create a new worktree at `../ordering-buy-now-service` linked to this branch

#### 2. New File: `BuyNowService.java`
**Path:** `src/main/java/com/lutz/algashop/ordering/domain/service/BuyNowService.java`

```java
@DomainService
public class BuyNowService {
    public Order buyNow(Product product, CustomerId customerId, 
                        Billing billing, Shipping shipping, 
                        Quantity quantity, PaymentMethod paymentMethod) {
        product.verifyIfIsInStock();              // 1. Stock validation
        Order order = Order.draft(customerId);     // 2. Create draft order
        order.changeBilling(billing);              // 3a. Set billing
        order.changeShipping(shipping);            // 3b. Set shipping
        order.changePaymentMethod(paymentMethod);  // 3c. Set payment method
        order.addItem(product, quantity);           // 4. Add item
        order.place();                             // 5. Place order
        return order;                              // 6. Return
    }
}
```

**Note:** `changeShipping()` must come before `addItem()` because `recalculateTotals()` needs shipping cost. The order of configuration calls matters for correct `totalAmount` calculation.

#### 3. Modified File: `ProductTestBuilder.java`
**Path:** `src/test/java/.../domain/entity/builder/ProductTestBuilder.java`

Add a new static builder method:
```java
public static Product.ProductBuilder aProductUnavailable() {
    return Product.builder()
            .id(new ProductId())
            .productName(new ProductName("Unavailable Product"))
            .price(new Money("25.00"))
            .inStock(false);
}
```

#### 4. New File: `BuyNowServiceTest.java`
**Path:** `src/test/java/com/lutz/algashop/ordering/domain/service/BuyNowServiceTest.java`

Test class structure using `@Nested` + `@DisplayName` (matching project conventions) with **AssertJ** assertions:

```
BuyNowServiceTest
├── @Nested BuyNowTests
│   ├── givenValidProductShouldCreatePlacedOrder
│   │   - Creates product (price=25.00, inStock=true), quantity=2
│   │   - Calls buyNow with billing, shipping (cost=10), paymentMethod
│   │   - Asserts: order is not null, isPlaced() is true
│   │   - Asserts: customerId, paymentMethod, billing, shipping match inputs
│   │   - Asserts: items has size 1, item has correct product/quantity
│   │   - Asserts: totalAmount = (25.00 * 2) + 10.00 = 60.00 (using Money VO)
│   │   - Asserts: itemsAmount = Quantity(2)
│   │
│   ├── givenOutOfStockProductShouldThrowProductOutOfStockException
│   │   - Uses aProductUnavailable().build()
│   │   - Asserts: throws ProductOutOfStockException
│   │
│   └── givenZeroQuantityShouldThrowIllegalArgumentException
│       - Uses valid product, quantity = Quantity(0)
│       - Asserts: throws IllegalArgumentException
```

All monetary assertions use `Money` VO (e.g., `assertThat(order.totalAmount()).isEqualTo(new Money("60.00"))`) — never raw `BigDecimal`.

### Execution Order

1. Create branch and worktree
2. Add `aProductUnavailable()` to `ProductTestBuilder.java`
3. Create `BuyNowService.java`
4. Create `BuyNowServiceTest.java`
5. Run tests to verify everything passes