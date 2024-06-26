package com.jsp.esa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.esa.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public boolean existsByEmail(String email);

}
