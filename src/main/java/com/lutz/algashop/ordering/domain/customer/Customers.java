package com.lutz.algashop.ordering.domain.customer;

import com.lutz.algashop.ordering.domain.Repository;
import com.lutz.algashop.ordering.domain.commons.Email;
import com.lutz.algashop.ordering.domain.customer.vo.CustomerId;

import java.util.Optional;

public interface Customers extends Repository<Customer, CustomerId> {
	Optional<Customer> ofEmail(Email email);
	boolean isEmailUnique(Email email, CustomerId exception);
}
