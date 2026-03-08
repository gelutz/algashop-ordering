package com.lutz.algashop.ordering.infrastructure.beans;

import com.lutz.algashop.ordering.domain.utils.annotations.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
		basePackages = "com.lutz.algashop.ordering.domain",
		includeFilters = @ComponentScan.Filter(
				type = FilterType.ANNOTATION,
				classes = DomainService.class
		)
)
public class DomainServiceScanConfig {
}
