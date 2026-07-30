package com.lutz.algashop.ordering.application.commons;

import com.lutz.algashop.ordering.domain.commons.Address;
import com.lutz.algashop.ordering.domain.commons.ZipCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressData {
	@NotBlank
	private String street;
	@NotBlank
	private String number;
	private String complement;
	@NotBlank
	private String neighborhood;
	@NotBlank
	private String city;
	@NotBlank
	private String state;
	@NotBlank
	private String zipCode;

	public static Address toDomain(AddressData data) {
		return Address.builder()
		              .street(data.getStreet())
		              .number(data.getNumber())
		              .complement(data.getComplement())
		              .neighborhood(data.getNeighborhood())
		              .city(data.getCity())
		              .state(data.getState())
		              .zipCode(new ZipCode(data.getZipCode()))
		              .build();
	}

	public static AddressData fromDomain(Address data) {
		return AddressData.builder()
		                  .street(data.street())
		                  .number(data.number())
		                  .complement(data.complement())
		                  .neighborhood(data.neighborhood())
		                  .city(data.city())
		                  .state(data.state())
		                  .zipCode(data.zipCode().toString())
		                  .build();
	}
}
