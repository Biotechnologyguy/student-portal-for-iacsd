package com.student.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.entites.User;
import com.student.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public User addUser(User s) {
		return userRepo.save(s);
	}

	@Override
	public boolean checkEmail(String email) {
		return userRepo.existsByEmail(email);
	}

	@Override
	public User getUserById(long id) {
		return userRepo.findById(id).get();
	}

	@Override
	public User updateUser(User s) {

		User oldStudent = userRepo.findById(s.getId()).get();
		s.setPassword(oldStudent.getPassword());
		s.setRole(oldStudent.getRole());
		return userRepo.save(s);
	}

	@Override
	public boolean deleteUser(long id) {

		User s = userRepo.findById(id).get();

		if (s != null) {
			userRepo.delete(s);
			return true;
		}

		return false;
	}

	@Override
	public List<User> getAllStudent() {
		return userRepo.findByRole("ROLE_USER");
	}

	@Override
	public List<User> getAllFaculity() {
		return userRepo.findByRole("ROLE_FACULITY");
	}

}
