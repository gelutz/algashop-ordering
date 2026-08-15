package com.lutz.algashop.ordering.application.order.query.detail;

import com.lutz.algashop.ordering.application.order.query.BillingData;
import com.lutz.algashop.ordering.application.order.query.CustomerMinimalOutput;
import com.lutz.algashop.ordering.application.order.query.ShippingData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailOutput {
	private String id;
	private CustomerMinimalOutput customer;
	private Integer totalItems;
	private BigDecimal totalAmount;
	private OffsetDateTime placedAt;
	private OffsetDateTime paidAt;
	private OffsetDateTime readyAt;
	private OffsetDateTime canceledAt;
	private String status;
	private String paymentMethod;
	private ShippingData shipping;
	private BillingData billing;

	private List<OrderItemDetailOutput> items;
}
