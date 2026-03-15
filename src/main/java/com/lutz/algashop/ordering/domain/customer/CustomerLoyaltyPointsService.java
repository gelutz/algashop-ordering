package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.DomainService;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFromThisCustomerException;
import com.lutz.algashop.ordering.domain.order.exception.WrongOrderStatusException;
import lombok.NonNull;

@DomainService
public class CustomerLoyaltyPointsService {
	public static final LoyaltyPoints multiplier = new LoyaltyPoints(1);
	public static final Money VALUE_PER_POINT = new Money("100");

	public void addPoints(@NonNull Customer customer, @NonNull Order order) {
		if (!customer.id().equals(order.customerId())) {
			throw new OrderNotFromThisCustomerException(order.id(), customer.id());
		}

		if (!order.isReady()) {
			throw new WrongOrderStatusException(order.id(), order.status(), OrderStatus.READY);
		}

		customer.addLoyaltyPoints(calculatePoints(order));
	}

	private LoyaltyPoints calculatePoints(@NonNull Order order) {
		if (!shouldGivePointsByAmount(order.totalAmount())) {
			return LoyaltyPoints.ZERO;
		}

		int points = order.totalAmount().divide(VALUE_PER_POINT).value().intValue();

		return new LoyaltyPoints(points * multiplier.value());
	}

	private boolean shouldGivePointsByAmount(Money money) {

		return money.compareTo(VALUE_PER_POINT) > 0;
	}
}
