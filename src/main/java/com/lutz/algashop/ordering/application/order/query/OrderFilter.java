package com.lutz.algashop.ordering.application.order.query;

import com.lutz.algashop.ordering.application.utility.SortablePageFilter;
import lombok.*;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderFilter extends SortablePageFilter<OrderFilter.SortType> {
	@Getter
	@RequiredArgsConstructor
	public enum SortType {
		STATUS("status"),
		PLACED_AT("placedAt"),
		PAID_AT("paidAt"),
		READY_AT("readyAt"),
		CANCELED_AT("canceledAt"),
		ID("id")
		;

		private final String propertyName;
	}

	private String status;
	private String orderId;
	private UUID customerId;
	private OffsetDateTime placedAtFrom;
	private OffsetDateTime placedAtTo;
	private BigDecimal totalAmountFrom;
	private BigDecimal totalAmountTo;

	public OrderFilter(int size, int page) {
		super(size, page);
	}

	@Override
	public SortType getSortByPropertyOrDefault() {
		return getSortByProperty() != null ? getSortByProperty() : SortType.PLACED_AT;
	}

	@Override
	public Sort.Direction getSortDirectionOrDefault() {
		return getSortDirection() != null ? getSortDirection() : Sort.Direction.ASC;
	}
}
