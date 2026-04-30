package com.lutz.algashop.ordering.application.order.query;

import com.lutz.algashop.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingData {
	private BigDecimal cost;
	private LocalDate expectedDate;
	private RecipientData recipient;
	private AddressData address;
}
