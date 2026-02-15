package com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AddressEmbeddable {
	private String street;
	private String number;
	private String complement;
	private String neighborhood;
	private String city;
	private String state;
	private String zipCode;
}
