package com.lutz.algashop.ordering.domain.entity;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.exception.CannotChangeOrderStatusException;
import com.lutz.algashop.ordering.domain.vo.*;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order {
	private OrderId id;
	private CustomerId customerId;
	private Money totalAmount;
	private Quantity itemsAmount;
	private OffsetDateTime paidAt;
	private OffsetDateTime placedAt;
	private OffsetDateTime canceledAt;
	private OffsetDateTime readyAt;

	private BillingInfo billingInfo;
	private ShippingInfo shippingInfo;

	private OrderStatus status;
	private PaymentMethod paymentMethod;

	private Money shippingCost;
	private LocalDate expectedDeliveryDate;

	private Set<OrderItem> items;

	@Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
	private Order(OrderId id, CustomerId customerId, Money totalAmount, Quantity itemsAmount, OffsetDateTime paidAt, OffsetDateTime placedAt, OffsetDateTime canceledAt, OffsetDateTime readyAt, BillingInfo billingInfo, ShippingInfo shippingInfo, OrderStatus status, PaymentMethod paymentMethod, Money shippingCost, LocalDate expectedDeliveryDate, Set<OrderItem> items) {
		this.setId(id);
		this.setCustomerId(customerId);
		this.setTotalAmount(totalAmount);
		this.setItemsAmount(itemsAmount);
		this.setPaidAt(paidAt);
		this.setPlacedAt(placedAt);
		this.setCanceledAt(canceledAt);
		this.setReadyAt(readyAt);
		this.setBillingInfo(billingInfo);
		this.setShippingInfo(shippingInfo);
		this.setStatus(status);
		this.setPaymentMethod(paymentMethod);
		this.setShippingCost(shippingCost);
		this.setExpectedDeliveryDate(expectedDeliveryDate);
		this.setItems(items);
	}

	public static Order draft(CustomerId customerId) {
		return new Order(
				new OrderId(),
				customerId,
				Money.ZERO,
				Quantity.ZERO,
				null,
				null,
				null,
				null,
				null,
				null,
				OrderStatus.DRAFT,
				null,
				null,
				null,
				new HashSet<>()
		);
	}

	public void addItem(ProductId productId, ProductName productName, Money price, Quantity quantity) {
		if (this.items == null) this.setItems(new HashSet<>());

		this.items.add(OrderItem.newOrderBuilder()
		                        .orderId(id())
		                        .productId(productId)
		                        .productName(productName)
		                        .price(price)
		                        .quantity(quantity)
		                        .build());

		this.recalculateTotals();
	}

	public void place() {
		this.changeStatus(OrderStatus.PLACED);
	}

	private void changeStatus(@NonNull OrderStatus orderStatus) {
		if (this.status().cannotChangeTo(orderStatus)) {
			throw new CannotChangeOrderStatusException(this.id(), this.status(), orderStatus);
		}
	}

	public BillingInfo billingInfo() {
		return billingInfo;
	}

	public OrderId id() {
		return id;
	}

	public CustomerId customerId() {
		return customerId;
	}

	public Money totalAmount() {
		return totalAmount;
	}

	public Quantity itemsAmount() {
		return itemsAmount;
	}

	public OffsetDateTime placedAt() {
		return placedAt;
	}

	public OffsetDateTime paidAt() {
		return paidAt;
	}

	public OffsetDateTime canceledAt() {
		return canceledAt;
	}

	public OffsetDateTime readyAt() {
		return readyAt;
	}

	public ShippingInfo shippingInfo() {
		return shippingInfo;
	}

	public OrderStatus status() {
		return status;
	}

	public PaymentMethod paymentMethod() {
		return paymentMethod;
	}

	public Money shippingCost() {
		return shippingCost;
	}

	public LocalDate expectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public Set<OrderItem> items() {
		return Collections.unmodifiableSet(this.items);
	}

	private void setId(@NonNull OrderId id) {
		this.id = id;
	}

	private void setCustomerId(@NonNull CustomerId customerId) {
		this.customerId = customerId;
	}

	private void setTotalAmount(@NonNull Money totalAmount) {
		this.totalAmount = totalAmount;
	}

	private void setItemsAmount(@NonNull Quantity itemsAmount) {
		this.itemsAmount = itemsAmount;
	}

	private void setPaidAt(OffsetDateTime paidAt) {
		this.paidAt = paidAt;
	}

	private void setPlacedAt(OffsetDateTime placedAt) {
		this.placedAt = placedAt;
	}

	private void setCanceledAt(OffsetDateTime canceledAt) {
		this.canceledAt = canceledAt;
	}

	private void setReadyAt(OffsetDateTime readyAt) {
		this.readyAt = readyAt;
	}

	private void setBillingInfo(BillingInfo billingInfo) {
		this.billingInfo = billingInfo;
	}

	private void setShippingInfo(ShippingInfo shippingInfo) {
		this.shippingInfo = shippingInfo;
	}

	private void setStatus(@NonNull OrderStatus status) {
		this.status = status;
	}

	private void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	private void setShippingCost(Money shippingCost) {
		this.shippingCost = shippingCost;
	}

	private void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	private void setItems(@NonNull Set<OrderItem> items) {
		this.items = items;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		Order order = (Order) o;
		return Objects.equals(id, order.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	private void recalculateTotals() {
		BigDecimal totalItemsCost = this.items()
		                                .stream()
		                                .map(i -> i.totalAmount().value())
		                                .reduce(BigDecimal.ZERO, (BigDecimal::add));

		Integer itemsQuantitySum = this.items()
		                               .stream()
		                               .map(i -> i.quantity().value())
		                               .reduce(0, Integer::sum);

		BigDecimal shippingCost = this.shippingCost() == null ? BigDecimal.ZERO : this.shippingCost().value();
		BigDecimal totalAmount = totalItemsCost.add(shippingCost);

		this.setTotalAmount(new Money(totalAmount));
		this.setItemsAmount(new Quantity(itemsQuantitySum));
	}
}
