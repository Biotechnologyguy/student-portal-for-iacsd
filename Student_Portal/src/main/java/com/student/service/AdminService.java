package com.student.service;

import java.util.List;

import com.student.entites.User;

public interface AdminService {

	public User addUser(User s);

	public boolean checkEmail(String email);

	public List<User> getAllStudent();
	
	public List<User> getAllFaculity();

	public User getUserById(long id);

	public User updateUser(User s);

	public boolean deleteUser(long id);

}
