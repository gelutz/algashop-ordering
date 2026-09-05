package com.lutz.algashop.ordering.presentation.customer;

import com.lutz.algashop.ordering.application.customer.management.CustomerInput;
import com.lutz.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.lutz.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.lutz.algashop.ordering.application.customer.query.CustomerFilter;
import com.lutz.algashop.ordering.application.customer.query.CustomerOutput;
import com.lutz.algashop.ordering.application.customer.query.CustomerQueryService;
import com.lutz.algashop.ordering.application.customer.query.CustomerSummaryOutput;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.lutz.algashop.ordering.presentation.PageModel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
	private final CustomerManagementApplicationService customerManagementApplicationService;
	private final CustomerQueryService customerQueryService;
	private final ShoppingCartQueryService shoppingCartQueryService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CustomerOutput create(@RequestBody @Valid CustomerInput input, HttpServletResponse httpServletResponse) {
		UUID customerId = customerManagementApplicationService.create(input);

		UriComponentsBuilder builder = MvcUriComponentsBuilder.fromMethodCall(
				MvcUriComponentsBuilder.on(CustomerController.class).findById(customerId)
		);

		httpServletResponse.addHeader("Location", builder.toUriString());
		return customerQueryService.findById(customerId);
	}

	@GetMapping
	public PageModel<CustomerSummaryOutput> findAll(CustomerFilter customerFilter) {
		return PageModel.of(customerQueryService.filter(customerFilter));
	}

	@GetMapping("/{customerId}")
	public CustomerOutput findById(@PathVariable UUID customerId) {
		return customerQueryService.findById(customerId);
	}

	@GetMapping("/{customerId}/shopping-cart")
	public ShoppingCartOutput findShoppingCartByCustomerId(@PathVariable UUID customerId) {
		return shoppingCartQueryService.findByCustomerId(customerId);
	}

	@PutMapping("/{customerId}")
	public CustomerOutput update(@PathVariable UUID customerId, @RequestBody @Valid CustomerUpdateInput input) {
		customerManagementApplicationService.update(customerId, input);
		return customerQueryService.findById(customerId);
	}

	@DeleteMapping("/{customerId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID customerId) {
		customerManagementApplicationService.archive(customerId);
	}
}

