package com.jsp.esa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.esa.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{


}
