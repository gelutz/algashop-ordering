package com.lutz.algashop.ordering.domain.entity.customer.vo;

import com.lutz.algashop.ordering.domain.entity.customer.vo.Address;
import com.lutz.algashop.ordering.domain.entity.customer.vo.ZipCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressTest {
	@Test
	void givenBlankValuesConstructorShouldThrowIllegalArgumentException() {
		ZipCode validZipCode = new ZipCode("123123");
		Address sut = Address.builder()
		                     .street("This street")
		                     .number("102")
		                     .complement("near the thing")
		                     .neighborhood("that one")
		                     .city("Brooklyn")
		                     .state("New York")
		                     .zipCode(validZipCode)
		                     .build();
		Assertions.assertThrows(IllegalArgumentException.class, () -> sut.toBuilder().street("").build());
		Assertions.assertThrows(IllegalArgumentException.class, () -> sut.toBuilder().number("").build());
		Assertions.assertThrows(IllegalArgumentException.class, () -> sut.toBuilder().neighborhood("").build());
		Assertions.assertThrows(IllegalArgumentException.class, () -> sut.toBuilder().city("").build());
		Assertions.assertThrows(IllegalArgumentException.class, () -> sut.toBuilder().state("").build());
	}
}