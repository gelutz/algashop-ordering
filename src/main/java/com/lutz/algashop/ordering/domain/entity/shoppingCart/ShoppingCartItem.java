package com.lutz.algashop.ordering.domain.entity.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.domain.exception.shoppingCart.ShoppingCartItemIncompatibleProductException;
import lombok.Builder;
import lombok.NonNull;

import java.util.Objects;

public class ShoppingCartItem {
	public ShoppingCartItemId id;
	public ShoppingCartId shoppingCartId;
	public ProductId productId;
	public ProductName name;
	public Money price;
	public Quantity quantity;
	public Money totalAmount;
	public Boolean available;

	@Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existing")
	private ShoppingCartItem(@NonNull ShoppingCartItemId id, @NonNull ShoppingCartId shoppingCartId, @NonNull ProductId productId, @NonNull ProductName name, @NonNull Money price, @NonNull Quantity quantity, @NonNull Money totalAmount, @NonNull Boolean available) {
		setId(id);
		setShoppingCartId(shoppingCartId);
		setProductId(productId);
		setName(name);
		setPrice(price);
		setQuantity(quantity);
		setTotalAmount(totalAmount);
		setAvailable(available);
	}

	public static ShoppingCartItem fresh(ShoppingCartId shoppingCartId, Product product, Quantity quantity) {
		return new ShoppingCartItem(
				new ShoppingCartItemId(),
				shoppingCartId,
				product.id(),
				product.productName(),
				product.price(),
				quantity,
				calculateTotalAmount(product.price(), quantity),
				product.inStock()
		);
	}

	void refresh(Product product) {
		if (!this.productId().equals(product.id())) {
			throw new ShoppingCartItemIncompatibleProductException(this.id(), this.productId(), product.id());
		}

		setName(product.productName());
		setPrice(product.price());
		setAvailable(product.inStock());

		recalculateTotals();
	}

	void changeQuantity(Quantity quantity) {
		if (quantity.value() <= 0) {
			throw new IllegalArgumentException();
		}

		this.setQuantity(quantity);

		recalculateTotals();
	}

	public ShoppingCartItemId id() {
		return id;
	}

	public ShoppingCartId shoppingCartId() {
		return shoppingCartId;
	}

	public ProductId productId() {
		return productId;
	}

	public ProductName name() {
		return name;
	}

	public Money price() {
		return price;
	}

	public Quantity quantity() {
		return quantity;
	}

	public Money totalAmount() {
		return totalAmount;
	}

	public Boolean available() {
		return available;
	}

	private void recalculateTotals() {
		setTotalAmount(calculateTotalAmount(price(), quantity()));
	}

	private static Money calculateTotalAmount(Money price, Quantity quantity) {
		return price.multiply(quantity);
	}

	private void setId(@NonNull ShoppingCartItemId id) {
		this.id = id;
	}
	private void setShoppingCartId(@NonNull ShoppingCartId shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}
	private void setProductId(@NonNull ProductId productId) {
		this.productId = productId;
	}
	private void setName(@NonNull ProductName name) {
		this.name = name;
	}
	private void setPrice(@NonNull Money price) {
		this.price = price;
	}
	private void setQuantity(@NonNull Quantity quantity) {
		this.quantity = quantity;
	}
	private void setTotalAmount(@NonNull Money totalAmount) {
		this.totalAmount = totalAmount;
	}
	private void setAvailable(@NonNull Boolean available) {
		this.available = available;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		ShoppingCartItem that = (ShoppingCartItem) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
