package com.lutz.algashop.ordering.infrastructure.fake.service;

import com.lutz.algashop.ordering.domain.entity.customer.vo.Address;
import com.lutz.algashop.ordering.domain.entity.customer.vo.ZipCode;
import com.lutz.algashop.ordering.domain.service.OriginAddressService;
import org.springframework.stereotype.Component;

@Component
public class FixedOriginAddressFakeImpl implements OriginAddressService {
	@Override
	public Address originAddress() {
		return new Address("Street", "123", "Neighborhood", "City", "State", "12345-678", new ZipCode("123123"));
	}
}
