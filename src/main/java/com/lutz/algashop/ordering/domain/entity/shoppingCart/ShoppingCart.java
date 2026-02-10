package com.lutz.algashop.ordering.domain.entity.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Accessors(fluent = true)
public class ShoppingCart {

	private ShoppingCartId id;
	private CustomerId customerId;
	private Money totalAmount;
	private Quantity totalItems;
	private OffsetDateTime createdAt;

	@Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existing")
	private ShoppingCart(@NonNull ShoppingCartId id, @NonNull CustomerId customerId, @NonNull Money totalAmount, @NonNull Quantity totalItems, @NonNull OffsetDateTime createdAt) {
		setId(id);
		setCustomerId(customerId);
		setTotalAmount(totalAmount);
		setTotalItems(totalItems);
		setCreatedAt(createdAt);
	}

	public static ShoppingCart fresh(CustomerId customerId) {
		return new ShoppingCart(
				new ShoppingCartId(),
				customerId,
				Money.ZERO,
				Quantity.ZERO,
				OffsetDateTime.now()
		);
	}

	public static ShoppingCart startShopping(CustomerId customerId) {
		throw new RuntimeException("Method not yet implemented.");
	}
	public void empty() {
		throw new RuntimeException("Method not yet implemented.");
	}
	public ShoppingCartItem findItem(ShoppingCartItemId itemId) {
		throw new RuntimeException("Method not yet implemented.");
	}
	public ShoppingCartItem findItem(ProductId productId) {
		throw new RuntimeException("Method not yet implemented.");
	}
	public void addItem(Product product, Quantity quantity) {
		throw new RuntimeException("Method not yet implemented.");
	}
	public void refreshItem(Product product) {
		throw new RuntimeException("Method not yet implemented.");
	}
	public void removeItem(ShoppingCartItemId id) {
		throw new RuntimeException("Method not yet implemented.");
	}
	public void changeItemQuantity(ShoppingCartItemId itemId, Quantity quantity) {
		throw new RuntimeException("Method not yet implemented.");
	}
	public boolean containsUnavailableItems() {
		throw new RuntimeException("Method not yet implemented.");
	}
	public boolean isEmpty() {
		return this.totalItems.value() == 0;
	}

	private ShoppingCart setId(ShoppingCartId id) {
		this.id = id;
		return this;
	}

	private ShoppingCart setTotalAmount(Money totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	private ShoppingCart setCustomerId(CustomerId customerId) {
		this.customerId = customerId;
		return this;
	}

	private ShoppingCart setTotalItems(Quantity totalItems) {
		this.totalItems = totalItems;
		return this;
	}

	private ShoppingCart setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		ShoppingCart that = (ShoppingCart) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
