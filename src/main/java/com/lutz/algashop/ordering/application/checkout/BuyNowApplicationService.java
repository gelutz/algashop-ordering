package com.lutz.algashop.ordering.application.checkout;

import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.commons.ZipCode;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.order.Billing;
import com.lutz.algashop.ordering.domain.order.BuyNowService;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.PaymentMethod;
import com.lutz.algashop.ordering.domain.order.shipping.OriginAddressService;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;
import com.lutz.algashop.ordering.domain.order.shipping.ShippingCostService;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductCatalogService;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.product.ProductNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuyNowApplicationService {

	private final BuyNowService buyNowService;
	private final ProductCatalogService productCatalogService;
	private final ShippingCostService shippingCostService;
	private final OriginAddressService originAddressService;

	private final Orders orders;

	private final BillingInputDisassembler billingInputDisassembler;
	private final ShippingInputDisassembler shippingInputDisassembler;

	@Transactional
	public String buyNow(@NonNull BuyNowInput input) {
		PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());
		CustomerId customerId = new CustomerId(input.getCustomerId());
		Quantity quantity = new Quantity(input.getQuantity());

		Product product = findProduct(new ProductId(input.getProductId()));

		var calculationResult = calculateShippingCost(input.getShipping());

		Shipping shipping = shippingInputDisassembler.toDomainModel(input.getShipping(), calculationResult);

		Billing billing = billingInputDisassembler.toDomainModel(input.getBilling());

		Order order = buyNowService.buyNow(
				product, customerId, billing, shipping, quantity, paymentMethod
		                                  );
		orders.add(order);

		return order.id().toString();
	}

	private ShippingCostService.CalculationResult calculateShippingCost(ShippingInput shipping) {
		ZipCode origin = originAddressService.originAddress().zipCode();
		ZipCode destination = new ZipCode(shipping.getAddress().getZipCode());
		return shippingCostService.calculate(new ShippingCostService.CalculationRequest(origin, destination));

	}

	private Product findProduct(ProductId productId) {
		return productCatalogService.ofId(productId)
				.orElseThrow(() -> new ProductNotFoundException(productId));
	}
}
