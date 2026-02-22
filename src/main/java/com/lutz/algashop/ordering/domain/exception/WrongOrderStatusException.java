package com.lutz.algashop.ordering.domain.exception;

import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderId;
import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;

public class WrongOrderStatusException extends DomainException {
	public WrongOrderStatusException(OrderId id, OrderStatus is, OrderStatus shouldBe) {
		super(ErrorMessages.Orders.orderHasWrongStatus(id, is, shouldBe));
	}
}
