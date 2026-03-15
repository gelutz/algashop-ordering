package com.lutz.algashop.ordering.infrastructure.shipping.client.fake;

import com.lutz.algashop.ordering.domain.commons.Address;
import com.lutz.algashop.ordering.domain.commons.ZipCode;
import com.lutz.algashop.ordering.domain.order.shipping.OriginAddressService;
import org.springframework.stereotype.Component;

@Component
public class FixedOriginAddressFakeImpl implements OriginAddressService {
	@Override
	public Address originAddress() {
		return new Address("Street", "123", "Neighborhood", "City", "State", "12345-678", new ZipCode("123123"));
	}
}
