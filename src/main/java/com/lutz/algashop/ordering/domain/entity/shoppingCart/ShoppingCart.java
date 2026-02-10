package com.lutz.algashop.ordering.domain.entity.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.domain.exception.shoppingCart.ShoppingCartDoesNotContainProduct;
import com.lutz.algashop.ordering.domain.exception.shoppingCart.ShoppingCartDoesNotContainItemException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Accessors(fluent = true)
public class ShoppingCart {

	private ShoppingCartId id;
	private CustomerId customerId;
	private Set<ShoppingCartItem> items;
	private Money totalAmount;
	private Quantity totalItems;
	private OffsetDateTime createdAt;

	@Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existing")
	private ShoppingCart(@NonNull ShoppingCartId id, @NonNull CustomerId customerId, @NonNull Money totalAmount, @NonNull Quantity totalItems, @NonNull OffsetDateTime createdAt, Set<ShoppingCartItem> items) {
		setId(id);
		setCustomerId(customerId);
		setTotalAmount(totalAmount);
		setTotalItems(totalItems);
		setCreatedAt(createdAt);
		setItems(items);
	}

	public static ShoppingCart startShopping(@NonNull CustomerId customerId) {
		return new ShoppingCart(
				new ShoppingCartId(),
				customerId,
				Money.ZERO,
				Quantity.ZERO,
				OffsetDateTime.now(),
				new HashSet<>()
		);
	}

	public Set<ShoppingCartItem> items() {
		return Collections.unmodifiableSet(this.items);
	}

	public void empty() {
		this.items.clear();
		this.totalAmount = Money.ZERO;
		this.totalItems = Quantity.ZERO;
	}

	public ShoppingCartItem findItem(@NonNull ShoppingCartItemId itemId) {
		return items()
				.stream()
				.filter(item -> item.id().equals(itemId))
				.findFirst()
				.orElseThrow(() -> new ShoppingCartDoesNotContainItemException(this.id(), itemId));
	}

	public ShoppingCartItem findItem(@NonNull ProductId productId) {
		return items()
				.stream()
				.filter(item -> item.productId().equals(productId))
				.findFirst()
				.orElseThrow(() -> new ShoppingCartDoesNotContainProduct(this.id(), productId));
	}

	public void addItem(@NonNull Product product, @NonNull Quantity quantity) {
		product.verifyIfIsInStock();
		ShoppingCartItem existingItem = items()
				.stream()
				.filter(i -> i.productId().equals(product.id()))
				.findFirst()
				.orElse(null);

		if (existingItem != null) {
			existingItem.changeQuantity(existingItem.quantity().add(quantity));
		} else {
			this.items.add(ShoppingCartItem.fresh(this.id(), product, quantity));
		}

		recalculateTotals();
	}

	public void refreshItem(@NonNull Product product) {
		findItem(product.id()).refresh(product);

		recalculateTotals();
	}

	public void removeItem(@NonNull ShoppingCartItemId id) {
		this.items.remove(findItem(id));

		recalculateTotals();
	}

	public void changeItemQuantity(@NonNull ShoppingCartItemId itemId, @NonNull Quantity quantity) {
		findItem(itemId).changeQuantity(quantity);

		recalculateTotals();
	}

	public boolean containsUnavailableItems() {
		return items().stream().anyMatch(i -> !i.available());
	}

	public boolean isEmpty() {
		return this.totalItems.value() == 0;
	}

	private void recalculateTotals() {
		BigDecimal totalAmount = this.items()
		                                .stream()
		                                .map(i -> i.totalAmount().value())
		                                .reduce(BigDecimal.ZERO, (BigDecimal::add));

		Integer itemsQuantitySum = this.items()
		                               .stream()
		                               .map(i -> i.quantity().value())
		                               .reduce(0, Integer::sum);

		this.setTotalAmount(new Money(totalAmount));
		this.setTotalItems(new Quantity(itemsQuantitySum));
	}

	/** SETTERS **/

	private void setId(@NonNull ShoppingCartId id) {
		this.id = id;
	}

	private void setTotalAmount(@NonNull Money totalAmount) {
		this.totalAmount = totalAmount;
	}

	private void setCustomerId(@NonNull CustomerId customerId) {
		this.customerId = customerId;
	}

	private void setItems(@NonNull Set<ShoppingCartItem> items) {
		this.items = items;
	}

	private void setTotalItems(@NonNull Quantity totalItems) {
		this.totalItems = totalItems;
	}

	private void setCreatedAt(@NonNull OffsetDateTime createdAt) {
		this.createdAt = createdAt;
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
