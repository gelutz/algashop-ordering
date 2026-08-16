package com.lutz.algashop.ordering.presentation;

import com.lutz.algashop.ordering.application.checkout.BuyNowApplicationService;
import com.lutz.algashop.ordering.application.checkout.BuyNowInput;
import com.lutz.algashop.ordering.application.checkout.CheckoutApplicationService;
import com.lutz.algashop.ordering.application.checkout.CheckoutInput;
import com.lutz.algashop.ordering.application.order.query.OrderFilter;
import com.lutz.algashop.ordering.application.order.query.OrderQueryService;
import com.lutz.algashop.ordering.application.order.query.detail.OrderDetailOutput;
import com.lutz.algashop.ordering.application.order.query.summary.OrderSummaryOutput;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderQueryService orderQueryService;
	private final CheckoutApplicationService checkoutApplicationService;
	private final BuyNowApplicationService buyNowApplicationService;

	@GetMapping
	public PageModel<OrderSummaryOutput> findAll(OrderFilter orderFilter) {
		return PageModel.of(orderQueryService.filter(orderFilter));
	}

	@GetMapping("/{orderId}")
	public OrderDetailOutput findById(@PathVariable String orderId) {
		return orderQueryService.findById(orderId);
	}

	@PostMapping(consumes = "application/vnd.order-with-product.v1+json")
	@ResponseStatus(HttpStatus.CREATED)
	public OrderDetailOutput buyNow(@RequestBody @Valid BuyNowInput input, HttpServletResponse httpServletResponse) {
		String orderId = buyNowApplicationService.buyNow(input);
		return findByIdWithLocation(orderId, httpServletResponse);
	}

	@PostMapping(consumes = "application/vnd.order-with-shopping-cart.v1+json")
	@ResponseStatus(HttpStatus.CREATED)
	public OrderDetailOutput checkout(@RequestBody @Valid CheckoutInput input, HttpServletResponse httpServletResponse) {
		String orderId = checkoutApplicationService.checkout(input);
		return findByIdWithLocation(orderId, httpServletResponse);
	}

	private OrderDetailOutput findByIdWithLocation(String orderId, HttpServletResponse httpServletResponse) {
		UriComponentsBuilder builder = MvcUriComponentsBuilder.fromMethodCall(
				MvcUriComponentsBuilder.on(OrderController.class).findById(orderId)
		);

		httpServletResponse.addHeader("Location", builder.toUriString());
		return orderQueryService.findById(orderId);
	}
}
