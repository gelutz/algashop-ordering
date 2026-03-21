package com.lutz.algashop.ordering.application.checkout;

import com.lutz.algashop.ordering.application.commons.AddressData;
import com.lutz.algashop.ordering.domain.commons.*;
import com.lutz.algashop.ordering.domain.order.Recipient;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;
import com.lutz.algashop.ordering.domain.order.shipping.ShippingCostService;
import org.springframework.stereotype.Component;

@Component
class ShippingInputDisassembler {

	public Shipping toDomainModel(ShippingInput shippingInput,
	                              ShippingCostService.CalculationResult shippingCalculationResult) {
		AddressData address = shippingInput.getAddress();
		return Shipping.builder()
		               .cost(shippingCalculationResult.cost())
		               .expectedDeliveryDate(shippingCalculationResult.expectedDate())
		               .recipient(Recipient.builder()
		                                   .fullName(new FullName(
				                                   shippingInput.getRecipient().getFirstName(),
				                                   shippingInput.getRecipient().getLastName()))
		                                   .document(new Document(shippingInput.getRecipient().getDocument()))
		                                   .phone(new Phone(shippingInput.getRecipient().getPhone()))
		                                   .build())
		               .address(Address.builder()
		                               .street(address.getStreet())
		                               .number(address.getNumber())
		                               .complement(address.getComplement())
		                               .neighborhood(address.getNeighborhood())
		                               .city(address.getCity())
		                               .state(address.getState())
		                               .zipCode(new ZipCode(address.getZipCode()))
		                               .build())
		               .build();
	}
}
