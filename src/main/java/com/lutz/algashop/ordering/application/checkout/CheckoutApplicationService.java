package com.lutz.algashop.ordering.application.checkout;

import com.lutz.algashop.ordering.domain.commons.ZipCode;
import com.lutz.algashop.ordering.domain.order.Billing;
import com.lutz.algashop.ordering.domain.order.CheckoutService;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.PaymentMethod;
import com.lutz.algashop.ordering.domain.order.shipping.OriginAddressService;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;
import com.lutz.algashop.ordering.domain.order.shipping.ShippingCostService;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCarts;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutApplicationService {

	private final CheckoutService checkoutService;
	private final ShippingCostService shippingCostService;
	private final OriginAddressService originAddressService;

	private final ShoppingCarts shoppingCarts;
	private final Orders orders;

	private final BillingInputDisassembler billingInputDisassembler;
	private final ShippingInputDisassembler shippingInputDisassembler;

	@Transactional
	public String checkout(@NonNull CheckoutInput input) {
		PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());

		ShoppingCartId shoppingCartId = new ShoppingCartId(input.getShoppingCartId());
		ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
				.orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));

		var calculationResult = calculateShippingCost(input.getShipping());

		Shipping shipping = shippingInputDisassembler.toDomainModel(input.getShipping(), calculationResult);
		Billing billing = billingInputDisassembler.toDomainModel(input.getBilling());

		Order order = checkoutService.checkout(shoppingCart, billing, shipping, paymentMethod);

		orders.add(order);
		shoppingCarts.add(shoppingCart);

		return order.id().toString();
	}

	private ShippingCostService.CalculationResult calculateShippingCost(ShippingInput shipping) {
		ZipCode origin = originAddressService.originAddress().zipCode();
		ZipCode destination = new ZipCode(shipping.getAddress().getZipCode());
		return shippingCostService.calculate(new ShippingCostService.CalculationRequest(origin, destination));
	}
}
