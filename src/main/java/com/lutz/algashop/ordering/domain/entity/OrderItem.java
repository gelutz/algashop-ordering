package com.lutz.algashop.ordering.domain.entity;

import com.lutz.algashop.ordering.domain.vo.*;
import lombok.Builder;
import lombok.NonNull;

public class OrderItem {
	private OrderItemId id;
	private OrderId orderId;
	private ProductId productId;
	private ProductName productName;
	private Money price;
	private Quantity quantity;
	private Money totalAmount;

	@Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
	private OrderItem(OrderItemId id, OrderId orderId, ProductId productId, ProductName productName, Money price, Quantity quantity, Money totalAmount) {
		this.setId(id);
		this.setOrderId(orderId);
		this.setProductId(productId);
		this.setProductName(productName);
		this.setPrice(price);
		this.setQuantity(quantity);
		this.setTotalAmount(totalAmount);
	}

	@Builder(builderClassName = "NewOrderBuilder", builderMethodName = "newOrderBuilder")
	private static OrderItem from(OrderId orderId, ProductId productId, ProductName productName, Money price, Quantity quantity) {
		return new OrderItem(
				new OrderItemId(),
				orderId,
				productId,
				productName,
				price,
				quantity,
				Money.ZERO
		);
	}

	public void setId(@NonNull OrderItemId id) {
		this.id = id;
	}

	public void setOrderId(@NonNull OrderId orderId) {
		this.orderId = orderId;
	}

	public void setProductId(@NonNull ProductId productId) {
		this.productId = productId;
	}

	public void setProductName(@NonNull ProductName productName) {
		this.productName = productName;
	}

	public void setPrice(@NonNull Money price) {
		this.price = price;
	}

	public void setQuantity(@NonNull Quantity quantity) {
		this.quantity = quantity;
	}

	public void setTotalAmount(@NonNull Money totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ProductName productName() {
		return productName;
	}

	public OrderItemId id() {
		return id;
	}

	public OrderId orderId() {
		return orderId;
	}

	public ProductId productId() {
		return productId;
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
}
