package com.lutz.algashop.ordering.domain.order.shipping;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.ZipCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.net.http.HttpClient;
import java.time.LocalDate;

@SpringBootTest
@Import(ShippingCostServiceIT.Http1ClientConfig.class)
class ShippingCostServiceIT {

	@TestConfiguration
	static class Http1ClientConfig {
		@Bean
		ClientHttpRequestFactoryBuilder<?> clientHttpRequestFactoryBuilder() {
			return ClientHttpRequestFactoryBuilder.jdk()
			                                       .withHttpClientCustomizer(builder -> builder.version(HttpClient.Version.HTTP_1_1));
		}
	}

	@Autowired
	private ShippingCostService shippingCostService;

	@Autowired
	private OriginAddressService originAddressService;

	@Value("${algashop.integrations.shipping.wiremock.port:8087}")
	private int rapidexWireMockPort;

	private WireMockServer rapidexWireMockServer;

	@BeforeEach
	void setup() {
		rapidexWireMockServer = new WireMockServer(
				WireMockConfiguration.options()
				                     .port(rapidexWireMockPort)
				                     .usingFilesUnderClasspath("wiremock/rapidex")
				                     .extensions(new ResponseTemplateTransformer(true))
		);
		rapidexWireMockServer.start();
	}

	@AfterEach
	void destroy() {
		rapidexWireMockServer.stop();
	}

	@Test
	void shouldCalculate() {
		ZipCode origin = originAddressService.originAddress().zipCode();
		ZipCode destination = new ZipCode("12345");

		ShippingCostService.CalculationResult result = shippingCostService.calculate(new ShippingCostService.CalculationRequest(
				origin,
				destination));

		Assertions.assertEquals(new Money("50.00"), result.cost());
		Assertions.assertEquals(LocalDate.now().plusDays(5), result.expectedDate());
	}

}
