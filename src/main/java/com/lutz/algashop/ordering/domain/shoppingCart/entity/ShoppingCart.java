package com.lutz.algashop.ordering.domain.shoppingCart.entity;

import com.lutz.algashop.ordering.domain.AbstractEventSourceEntity;
import com.lutz.algashop.ordering.domain.AggregateRoot;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartCreatedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartEmptiedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartItemAddedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartItemRemovedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartDoesNotContainItemException;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartDoesNotContainProduct;
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
public class ShoppingCart
		extends AbstractEventSourceEntity
		implements AggregateRoot<ShoppingCartId> {
	private ShoppingCartId id;
	private CustomerId customerId;
	private Set<ShoppingCartItem> items;
	private Money totalAmount;
	private Quantity totalItems;
	private OffsetDateTime createdAt;
	private Long version;

	@Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existing")
	private ShoppingCart(@NonNull ShoppingCartId id, @NonNull CustomerId customerId, @NonNull Money totalAmount, @NonNull Quantity totalItems, @NonNull OffsetDateTime createdAt, Set<ShoppingCartItem> items, Long version) {
		setId(id);
		setCustomerId(customerId);
		setTotalAmount(totalAmount);
		setTotalItems(totalItems);
		setCreatedAt(createdAt);
		setItems(items);
		setVersion(version);
	}

	public static ShoppingCart startShopping(@NonNull CustomerId customerId) {
		ShoppingCart shoppingCart = new ShoppingCart(
				new ShoppingCartId(),
				customerId,
				Money.ZERO,
				Quantity.ZERO,
				OffsetDateTime.now(),
				new HashSet<>(),
				null
		);
		shoppingCart.publishDomainEvent(new ShoppingCartCreatedEvent(shoppingCart.id(), shoppingCart.customerId(), shoppingCart.createdAt()));
		return shoppingCart;
	}

	public Set<ShoppingCartItem> items() {
		return Collections.unmodifiableSet(this.items);
	}

	public void empty() {
		this.items.clear();
		this.totalAmount = Money.ZERO;
		this.totalItems = Quantity.ZERO;
		this.publishDomainEvent(new ShoppingCartEmptiedEvent(id(), customerId(), OffsetDateTime.now()));
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
		this.publishDomainEvent(new ShoppingCartItemAddedEvent(id(), customerId(), product.id(), OffsetDateTime.now()));
	}

	public void refreshItem(@NonNull Product product) {
		findItem(product.id()).refresh(product);

		recalculateTotals();
	}

	public void removeItem(@NonNull ShoppingCartItemId id) {
		ShoppingCartItem item = findItem(id);
		ProductId productId = item.productId();
		this.items.remove(item);

		recalculateTotals();
		this.publishDomainEvent(new ShoppingCartItemRemovedEvent(id(), customerId(), productId, OffsetDateTime.now()));
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

	private void setVersion(Long version) {
		this.version = version;
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
