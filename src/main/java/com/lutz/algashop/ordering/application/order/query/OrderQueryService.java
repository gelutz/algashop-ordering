package com.lutz.algashop.ordering.application.order.query;

import com.lutz.algashop.ordering.application.order.query.detail.OrderDetailOutput;
import com.lutz.algashop.ordering.application.order.query.summary.OrderSummaryOutput;
import org.springframework.data.domain.Page;

public interface OrderQueryService {
	OrderDetailOutput findById(String id);

	Page<OrderSummaryOutput> filter(OrderFilter filter);
}
