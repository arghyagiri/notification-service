package com.tcs.training.customer.repository;

import com.tcs.training.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Customer findByEmailAddress(String emailAddress);

}
