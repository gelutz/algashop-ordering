package com.lutz.algashop.ordering.domain.repository;

import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.customer.vo.Email;

import java.util.Optional;

public interface Customers extends Repository<Customer, CustomerId> {
	Optional<Customer> ofEmail(Email email);
}
