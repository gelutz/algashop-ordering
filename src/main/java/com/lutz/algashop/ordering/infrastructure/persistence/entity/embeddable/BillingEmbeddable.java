package com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
		@AttributeOverride(name = "firstName", column = @Column(name = "billing_first_name")),
		@AttributeOverride(name = "lastName", column = @Column(name = "billing_last_name")),
		@AttributeOverride(name = "document", column = @Column(name = "billing_document")),
		@AttributeOverride(name = "phone", column = @Column(name = "billing_phone")),
		@AttributeOverride(name = "address.street", column = @Column(name = "billing_address_street")),
		@AttributeOverride(name = "address.number", column = @Column(name = "billing_address_number")),
		@AttributeOverride(name = "address.complement", column = @Column(name = "billing_address_complement")),
		@AttributeOverride(name = "address.neighborhood", column = @Column(name = "billing_address_neighborhood")),
		@AttributeOverride(name = "address.city", column = @Column(name = "billing_address_city")),
		@AttributeOverride(name = "address.state", column = @Column(name = "billing_address_state")),
		@AttributeOverride(name = "address.zipCode", column = @Column(name = "billing_address_zipCode"))
})
public class BillingEmbeddable {
	private String firstName;
	private String lastName;
	private String document;
	private String phone;
	private String email;
	@Embedded
	private AddressEmbeddable address;
}
