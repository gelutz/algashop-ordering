package com.lutz.algashop.ordering.domain.entity.order;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.domain.exception.InvalidShippingDeliveryDateException;
import com.lutz.algashop.ordering.domain.exception.order.OrderCannotBeEditedException;
import com.lutz.algashop.ordering.domain.exception.order.OrderCannotBePlacedException;
import com.lutz.algashop.ordering.domain.exception.order.OrderDoesNotContainOrderItemException;
import com.lutz.algashop.ordering.domain.exception.order.OrderStatusCannotBeChangedException;
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

	private Billing billing;
	private Shipping shipping;

	private OrderStatus status;
	private PaymentMethod paymentMethod;

	private Set<OrderItem> items;

	@Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
	private Order(OrderId id, CustomerId customerId, Money totalAmount, Quantity itemsAmount, OffsetDateTime paidAt, OffsetDateTime placedAt, OffsetDateTime canceledAt, OffsetDateTime readyAt, Billing billing, Shipping shipping, OrderStatus status, PaymentMethod paymentMethod, Set<OrderItem> items) {
		this.setId(id);
		this.setCustomerId(customerId);
		this.setTotalAmount(totalAmount);
		this.setItemsAmount(itemsAmount);
		this.setPaidAt(paidAt);
		this.setPlacedAt(placedAt);
		this.setCanceledAt(canceledAt);
		this.setReadyAt(readyAt);
		this.setBilling(billing);
		this.setShipping(shipping);
		this.setStatus(status);
		this.setPaymentMethod(paymentMethod);
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
				new HashSet<>()
		);
	}

	public void addItem(Product product, Quantity quantity) {
		if (this.items == null) this.setItems(new HashSet<>());

		product.verifyIfIsInStock();
		this.verifyIfChangeable();

		this.items.add(OrderItem.newOrderBuilder()
		                        .orderId(id())
			                    .product(product)
		                        .quantity(quantity)
		                        .build());

		this.recalculateTotals();
	}

	public void removeItem(@NonNull OrderItemId orderItemId) {
		this.verifyIfChangeable();

		OrderItem oi = findOrderItem(orderItemId);
		this.items.remove(oi);

		this.recalculateTotals();
	}

	public void place() {
		if (shipping() == null)
			throw OrderCannotBePlacedException.shippingInfoIsNull(this.id());
		if (billing() == null)
			throw OrderCannotBePlacedException.billingInfoIsNull(this.id());
		if (paymentMethod() == null)
			throw OrderCannotBePlacedException.paymentMethodIsNull(this.id());
		if (items() == null)
			throw OrderCannotBePlacedException.itemsIsNull(this.id());
		if (items.isEmpty())
			throw OrderCannotBePlacedException.emptyItems(this.id());

		this.setPlacedAt(OffsetDateTime.now());
		this.changeStatus(OrderStatus.PLACED);
	}

	public void pay() {
		this.setPaidAt(OffsetDateTime.now());
		this.changeStatus(OrderStatus.PAID);
	}

	public boolean isDraft() {
		return OrderStatus.DRAFT.equals(this.status());
	}
	public boolean isPlaced() {
		return OrderStatus.PLACED.equals(this.status());
	}
	public boolean isPaid() {
		return OrderStatus.PAID.equals(this.status());
	}
	public boolean isReady() {
		return OrderStatus.PLACED.equals(this.status());
	}
	public boolean isCanceled() {
		return OrderStatus.PLACED.equals(this.status());
	}

	public void changePaymentMethod(@NonNull PaymentMethod paymentMethod) {
		this.verifyIfChangeable();
		this.setPaymentMethod(paymentMethod);
	}

	public void changeBilling(@NonNull Billing billing) {
		this.verifyIfChangeable();
		this.setBilling(billing);
	}

	public void changeShipping(@NonNull Shipping newShipping) {
		if (newShipping.expectedDeliveryDate().isBefore(LocalDate.now())) {
			throw new InvalidShippingDeliveryDateException(this.id(), newShipping.expectedDeliveryDate());
		}

		this.verifyIfChangeable();

		this.setShipping(newShipping);
		this.recalculateTotals();
	}

	public void changeItemQuantity(OrderItemId orderItemId, Quantity quantity) {
		this.verifyIfChangeable();

		OrderItem orderItem = findOrderItem(orderItemId);

		orderItem.changeQuantity(quantity);

		this.recalculateTotals();
	}

	public Billing billing() {
		return billing;
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

	public Shipping shipping() {
		return shipping;
	}

	public OrderStatus status() {
		return status;
	}

	public PaymentMethod paymentMethod() {
		return paymentMethod;
	}

	public Set<OrderItem> items() {
		return Collections.unmodifiableSet(this.items);
	}



	private OrderItem findOrderItem(OrderItemId orderItemId) {
		return items()
				.stream()
				.filter(i -> i.id() == orderItemId)
				.findFirst()
				.orElseThrow(() -> new OrderDoesNotContainOrderItemException(this.id(), orderItemId));
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

		BigDecimal shippingCost = this.shipping() == null ? BigDecimal.ZERO : this.shipping().cost().value();
		BigDecimal totalAmount = totalItemsCost.add(shippingCost);

		this.setTotalAmount(new Money(totalAmount));
		this.setItemsAmount(new Quantity(itemsQuantitySum));
	}

	private void changeStatus(@NonNull OrderStatus orderStatus) {
		if (this.status().cannotChangeTo(orderStatus)) {
			throw new OrderStatusCannotBeChangedException(this.id(), this.status(), orderStatus);
		}

		this.verifyIfChangeable();
		this.setStatus(orderStatus);
	}

	private void verifyIfChangeable() {
		if (!this.isDraft()) throw new OrderCannotBeEditedException(this.id(), this.status());
	}

	/** SETTERS */
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

	private void setBilling(Billing billing) {
		this.billing = billing;
	}

	private void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	private void setStatus(@NonNull OrderStatus status) {
		this.status = status;
	}

	private void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
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
}
