package com.lutz.algashop.ordering.application.order.query;

import com.lutz.algashop.ordering.application.order.query.detail.OrderDetailOutput;
import com.lutz.algashop.ordering.application.order.query.summary.OrderSummaryOutput;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class OrderQueryServiceIT {
	@Autowired
	private OrderQueryService queryService;

	@Autowired
	private Orders orders;

	@Autowired
	private Customers customers;

	@Test
	public void shouldFindById() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		customers.add(customer);

		Order order = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer.id()).build();
		orders.add(order);

		OrderDetailOutput output = queryService.findById(order.id().toString());

		Assertions.assertEquals(order.id().toString(), output.getId());
		Assertions.assertEquals(order.totalAmount().value(), output.getTotalAmount());
	}

	@Test
	public void shouldFilterByPage() {
		Customer customer = CustomerTestBuilder
				.aCustomer()
				.build();
		customers.add(customer);

		Order order1 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer.id()).build();
		Order order2 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer.id()).build();
		Order order3 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer.id()).build();
		Order order4 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer.id()).build();
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		orders.add(order4);

		// se tem 4 elementos e o pagefilter busca 3 por página, vai ter um total de 2 páginas
		OrderFilter page = new OrderFilter(3, 0);
		Page<OrderSummaryOutput> output = queryService.filter(page);

		Assertions.assertEquals(2, output.getTotalPages());
		Assertions.assertEquals(page.getSize(), output.getNumberOfElements());
	}

	@Test
	public void shouldFilterByCustomerId() {
		Customer customer1 = CustomerTestBuilder.aCustomer().build();
		Customer customer2 = CustomerTestBuilder.aCustomer().withId(new CustomerId()).build();
		customers.add(customer1);
		customers.add(customer2);

		Order order1 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer1.id()).build();
		Order order2 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer1.id()).build();
		Order order3 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer2.id()).build();
		Order order4 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer2.id()).build();
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		orders.add(order4);

		OrderFilter filter = new OrderFilter(3, 0);
		filter.setCustomerId(customer1.id().value());
		Page<OrderSummaryOutput> output = queryService.filter(filter);

		Assertions.assertEquals(2, output.getNumberOfElements());
		Assertions.assertEquals(customer1.id().value(), output.getContent().getFirst().getCustomer().getId());
	}

	@Test
	public void shouldFilterByMultipleParams() {
		Customer customer1 = CustomerTestBuilder.aCustomer().build();
		Customer customer2 = CustomerTestBuilder.aCustomer().withId(new CustomerId()).build();
		customers.add(customer1);
		customers.add(customer2);

		Order order1 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer1.id()).build();
		Order order2 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer1.id()).build();
		Order order3 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer2.id()).build();
		Order order4 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer2.id()).build();
		order1.place();
		order2.place();
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		orders.add(order4);

		OrderFilter filter = new OrderFilter(3, 0);
		filter.setStatus(OrderStatus.PLACED.toString());
		filter.setTotalAmountFrom(order1.totalAmount().value());
		Page<OrderSummaryOutput> output = queryService.filter(filter);

		Assertions.assertEquals(2, output.getNumberOfElements());
		Assertions.assertEquals(customer1.id().value(), output.getContent().getFirst().getCustomer().getId());
	}

	@Test
	public void shouldFilterByInvalidId() {
		Customer customer1 = CustomerTestBuilder.aCustomer().build();
		customers.add(customer1);

		Order order1 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer1.id()).build();
		orders.add(order1);

		OrderFilter filter = new OrderFilter(3, 0);
		filter.setOrderId(new OrderId().toString());
		Page<OrderSummaryOutput> output = queryService.filter(filter);

		Assertions.assertEquals(0, output.getNumberOfElements());
	}

	@Test
	public void shouldSortBasedOnPageSorting() throws InterruptedException {
		Customer customer1 = CustomerTestBuilder.aCustomer().build();
		customers.add(customer1);

		Order order1 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer1.id()).build();
		Order order2 = OrderTestBuilder.aFilledDraftOrder().withCustomerId(customer1.id()).build();
		orders.add(order1);
		orders.add(order2);

		OrderFilter filter = new OrderFilter(3, 0);
		filter.setSortByProperty(OrderFilter.SortType.ID);
		Page<OrderSummaryOutput> output = queryService.filter(filter);

		Assertions.assertEquals(2, output.getNumberOfElements());
		Assertions.assertEquals(order1.id().toString(), output.getContent().getFirst().getId());

		order2.place();
		Thread.sleep(100);
		order1.place();

		orders.add(order1);
		orders.add(order2);
		filter.setSortByProperty(OrderFilter.SortType.PLACED_AT);
		filter.setSortDirection(Sort.Direction.ASC);
		output = queryService.filter(filter);

		Assertions.assertEquals(2, output.getNumberOfElements());
		Assertions.assertEquals(order2.id().toString(), output.getContent().getFirst().getId());
	}
}