package com.lutz.algashop.ordering.infrastructure.utility.modelmapper;

import com.lutz.algashop.ordering.application.customer.query.CustomerOutput;
import com.lutz.algashop.ordering.application.order.query.detail.OrderDetailOutput;
import com.lutz.algashop.ordering.application.order.query.detail.OrderItemDetailOutput;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartItemOutput;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.lutz.algashop.ordering.application.utility.Mapper;
import com.lutz.algashop.ordering.domain.commons.FullName;
import com.lutz.algashop.ordering.domain.customer.Birthdate;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.infrastructure.persistence.order.OrderItemPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart.ShoppingCartItemPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart.ShoppingCartPersistenceEntity;
import io.hypersistence.tsid.TSID;
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

	private static final Converter<Long, String> longToStringTSIDConverter =
			mappingContext -> {
				if (mappingContext.getSource() == null) {
					return null;
				}

				Long asLong = mappingContext.getSource();
				return new TSID(asLong).toString();
			};

	@Bean
	public Mapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		configuration(mapper);
		return
				mapper::map;
	}

	public void configuration(ModelMapper modelMapper) {
		modelMapper.getConfiguration()
				.setSourceNamingConvention(NamingConventions.NONE) // forces modelMapper to use more conventions than simply getters
				.setDestinationNamingConvention(NamingConventions.NONE) // forces modelMapper to use more conventions than simply getters
				.setMatchingStrategy(MatchingStrategies.STRICT);

		addCustomerMappings(modelMapper);
		addOrderPersistenceEntityMappings(modelMapper);
		addShoppingCartMappings(modelMapper);
	}

	private static void addOrderPersistenceEntityMappings(ModelMapper modelMapper) {
		modelMapper.createTypeMap(OrderPersistenceEntity.class, OrderDetailOutput.class)
		           .addMappings(mapping ->
						             mapping.using(longToStringTSIDConverter).map(OrderPersistenceEntity::getId, OrderDetailOutput::setId)
				);

		modelMapper.createTypeMap(OrderItemPersistenceEntity.class, OrderItemDetailOutput.class)
		           .addMappings(mapping ->
				                        mapping.using(longToStringTSIDConverter).map(OrderItemPersistenceEntity::getId, OrderItemDetailOutput::setId)
		           )
		           .addMappings(mapping ->
				                        mapping.using(longToStringTSIDConverter).map(OrderItemPersistenceEntity::getOrderId, OrderItemDetailOutput::setOrderId)
		           );
	}

	private static void addShoppingCartMappings(ModelMapper modelMapper) {
		modelMapper.createTypeMap(ShoppingCartPersistenceEntity.class, ShoppingCartOutput.class)
		           .addMappings(m -> m.map(
				           e -> e.getCustomer().getId(),
				           ShoppingCartOutput::setCustomerId
		           ));

		modelMapper.createTypeMap(ShoppingCartItemPersistenceEntity.class, ShoppingCartItemOutput.class)
		           .addMappings(m -> m.map(
				           ShoppingCartItemPersistenceEntity::getProductName,
				           ShoppingCartItemOutput::setName
		           ));
	}

	private static void addCustomerMappings(ModelMapper modelMapper) {
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
}
