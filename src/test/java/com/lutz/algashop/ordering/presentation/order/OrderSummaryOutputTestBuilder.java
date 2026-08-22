package com.lutz.algashop.ordering.presentation.order;

import com.lutz.algashop.ordering.application.order.query.CustomerMinimalOutput;
import com.lutz.algashop.ordering.application.order.query.summary.OrderSummaryOutput;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.order.OrderId;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderSummaryOutputTestBuilder {

	public static OrderSummaryOutput.OrderSummaryOutputBuilder placedOrder() {
		return OrderSummaryOutput.builder()
		                         .id(new OrderId().toString())
		                         .customer(CustomerMinimalOutput.builder()
		                                                        .id(new CustomerId().value())
		                                                        .firstName("John")
		                                                        .lastName("Doe")
		                                                        .document("12345")
		                                                        .email("johndoe@email.com")
		                                                        .phone("1191234564")
		                                                        .build())
		                         .totalItems(2)
		                         .totalAmount(new BigDecimal("60.48"))
		                         .placedAt(OffsetDateTime.now())
		                         .paidAt(null)
		                         .readyAt(null)
		                         .canceledAt(null)
		                         .status("PLACED")
		                         .paymentMethod("GATEWAY_BALANCE");
	}

	public static OrderSummaryOutput.OrderSummaryOutputBuilder placedOrderAlt1() {
		return OrderSummaryOutput.builder()
		                         .id(new OrderId().toString())
		                         .customer(CustomerMinimalOutput.builder()
		                                                        .id(new CustomerId().value())
		                                                        .firstName("Jane")
		                                                        .lastName("Roe")
		                                                        .document("54321")
		                                                        .email("janeroe@email.com")
		                                                        .phone("1187654321")
		                                                        .build())
		                         .totalItems(1)
		                         .totalAmount(new BigDecimal("19.99"))
		                         .placedAt(OffsetDateTime.now())
		                         .paidAt(null)
		                         .readyAt(null)
		                         .canceledAt(null)
		                         .status("PLACED")
		                         .paymentMethod("CREDIT_CARD");
	}
}
