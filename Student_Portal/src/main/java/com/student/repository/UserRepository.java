package com.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entites.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public boolean existsByEmail(String email);
	
	public User findByEmail(String email);
	
	public List<User> findByRole(String role);
}
