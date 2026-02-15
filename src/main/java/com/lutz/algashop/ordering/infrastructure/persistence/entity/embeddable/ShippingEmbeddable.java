package com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
		@AttributeOverride(name = "cost", column = @Column(name = "shipping_cost")),
		@AttributeOverride(name = "expectedDate", column = @Column(name = "shipping_expected_date")),
		@AttributeOverride(name = "recipient.firstName", column = @Column(name = "shipping_recipient_first_name")),
		@AttributeOverride(name = "recipient.lastName", column = @Column(name = "shipping_recipient_last_name")),
		@AttributeOverride(name = "recipient.document", column = @Column(name = "shipping_recipient_document")),
		@AttributeOverride(name = "recipient.phone", column = @Column(name = "shipping_recipient_phone")),
		@AttributeOverride(name = "address.street", column = @Column(name = "shipping_address_street")),
		@AttributeOverride(name = "address.number", column = @Column(name = "shipping_address_number")),
		@AttributeOverride(name = "address.complement", column = @Column(name = "shipping_address_complement")),
		@AttributeOverride(name = "address.neighborhood", column = @Column(name = "shipping_address_neighborhood")),
		@AttributeOverride(name = "address.city", column = @Column(name = "shipping_address_city")),
		@AttributeOverride(name = "address.state", column = @Column(name = "shipping_address_state")),
		@AttributeOverride(name = "address.zipCode", column = @Column(name = "shipping_address_zipCode"))
})
public class ShippingEmbeddable {
	private BigDecimal cost;
	private LocalDate expectedDeliveryDate;
	@Embedded
	private RecipientEmbeddable recipient;
	@Embedded
	private AddressEmbeddable address;
}
