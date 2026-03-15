package com.lutz.algashop.ordering.domain.order.exception;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.order.OrderId;

public class WrongOrderStatusException extends DomainException {
	public WrongOrderStatusException(OrderId id, OrderStatus is, OrderStatus shouldBe) {
		super(ErrorMessages.orderHasWrongStatus(id, is, shouldBe));
	}
}
