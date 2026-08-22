package com.lutz.algashop.ordering.presentation;

import com.lutz.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.lutz.algashop.ordering.application.customer.query.CustomerQueryService;
import com.lutz.algashop.ordering.presentation.customer.CustomerController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@WebMvcTest(controllers = CustomerController.class)
public abstract class AbstractCustomerControllerTest {

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@MockitoBean
	protected CustomerManagementApplicationService customerManagementApplicationServiceMock;

	@MockitoBean
	protected CustomerQueryService customerQueryServiceMock;

	protected final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
				                           .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				                           .build()
		);
		RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
	}
}
