package com.lutz.algashop.ordering.presentation.shoppingCart;

import com.lutz.algashop.ordering.application.shoppingcart.management.ShoppingCartInput;
import com.lutz.algashop.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import com.lutz.algashop.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartItemListModel;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@RequiredArgsConstructor
public class ShoppingCartController {
	private final ShoppingCartManagementApplicationService shoppingCartManagementApplicationService;
	private final ShoppingCartQueryService shoppingCartQueryService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ShoppingCartOutput create(@RequestBody @Valid ShoppingCartInput input, HttpServletResponse httpServletResponse) {
		UUID shoppingCartId = shoppingCartManagementApplicationService.createNew(input.getCustomerId());

		UriComponentsBuilder builder = MvcUriComponentsBuilder.fromMethodCall(
				MvcUriComponentsBuilder.on(ShoppingCartController.class).findById(shoppingCartId)
		);

		httpServletResponse.addHeader("Location", builder.toUriString());
		return shoppingCartQueryService.findById(shoppingCartId);
	}

	@GetMapping("/{shoppingCartId}")
	public ShoppingCartOutput findById(@PathVariable UUID shoppingCartId) {
		return shoppingCartQueryService.findById(shoppingCartId);
	}

	@GetMapping("/{shoppingCartId}/items")
	public ShoppingCartItemListModel findItems(@PathVariable UUID shoppingCartId) {
		ShoppingCartOutput shoppingCart = shoppingCartQueryService.findById(shoppingCartId);
		ShoppingCartItemListModel model = new ShoppingCartItemListModel();
		model.setItems(shoppingCart.getItems());
		return model;
	}

	@DeleteMapping("/{shoppingCartId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID shoppingCartId) {
		shoppingCartManagementApplicationService.delete(shoppingCartId);
	}

	@DeleteMapping("/{shoppingCartId}/items")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void empty(@PathVariable UUID shoppingCartId) {
		shoppingCartManagementApplicationService.empty(shoppingCartId);
	}

	@PostMapping("/{shoppingCartId}/items")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void addItem(@PathVariable UUID shoppingCartId, @RequestBody @Valid ShoppingCartItemInput input) {
		input.setShoppingCartId(shoppingCartId);
		shoppingCartManagementApplicationService.addItem(input);
	}

	@DeleteMapping("/{shoppingCartId}/items/{itemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeItem(@PathVariable UUID shoppingCartId, @PathVariable UUID itemId) {
		shoppingCartManagementApplicationService.removeItem(shoppingCartId, itemId);
	}
}
