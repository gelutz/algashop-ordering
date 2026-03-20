package com.lutz.algashop.ordering.infrastructure.utility.modelmapper;

import com.lutz.algashop.ordering.application.customer.management.CustomerOutput;
import com.lutz.algashop.ordering.application.utility.Mapper;
import com.lutz.algashop.ordering.domain.commons.FullName;
import com.lutz.algashop.ordering.domain.customer.Birthdate;
import com.lutz.algashop.ordering.domain.customer.Customer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NamingConventions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class ModelMapperConfig {
	private static final Converter<FullName, String> fullnameToFirstNameConverter =
			mappingContext -> {
				if (mappingContext.getSource() == null) {
					return null;
				}

				return mappingContext.getSource().firstName();
			};

	private static final Converter<FullName, String> fullnameToLastNameConverter =
			mappingContext -> {
				if (mappingContext.getSource() == null) {
					return null;
				}

				return mappingContext.getSource().lastName();
			};

	private static final Converter<Birthdate, LocalDate> birthdateToLocalDateConverter =
			mappingContext -> {
				if (mappingContext.getSource() == null) {
					return null;
				}

				return mappingContext.getSource().date();
			};

	public void configuration(ModelMapper modelMapper) {
		modelMapper.getConfiguration()
				.setSourceNamingConvention(NamingConventions.NONE) // forces modelMapper to use more conventions than simply getters
				.setDestinationNamingConvention(NamingConventions.NONE) // forces modelMapper to use more conventions than simply getters
				.setMatchingStrategy(MatchingStrategies.STRICT);

		modelMapper.createTypeMap(Customer.class, CustomerOutput.class)
				.addMappings(mapping ->
						mapping.using(fullnameToFirstNameConverter)
						       .map(Customer::fullName, CustomerOutput::setFirstName))
				.addMappings(mapping ->
						mapping.using(fullnameToLastNameConverter)
						       .map(Customer::fullName, CustomerOutput::setLastName))
				.addMappings(mapping ->
					mapping.using(birthdateToLocalDateConverter)
					       .map(Customer::birthdate, CustomerOutput::setBirthdate));
	}

	@Bean
	public Mapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		configuration(mapper);
		return mapper::map;
	}
}
