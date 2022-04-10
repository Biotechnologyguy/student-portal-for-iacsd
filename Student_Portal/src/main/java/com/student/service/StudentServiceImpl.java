package com.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.entites.AssignementAnswer;
import com.student.entites.User;
import com.student.repository.AssignmentAnswerRepository;
import com.student.repository.UserRepository;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private AssignmentAnswerRepository assignRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public AssignementAnswer uploadAssignment(AssignementAnswer answer) {
		return assignRepo.save(answer);
	}

	@Override
	public boolean checkUploadAnswerByUser(long userId, long assignmentId) {
		return assignRepo.existsByUserIdAndAssignmentId(userId, assignmentId);
	}

	@Override
	public String checkGradedByUser(long userId, long assignmentId) {
		AssignementAnswer as = assignRepo.findByUserIdAndAssignmentId(userId, assignmentId);
		if (as != null) {
			return as.getGrade();
		}
		return null;
	}

	@Override
	public User updateProfile(User u) {

		User oldUser = userRepo.findById(u.getId()).get();

		if (oldUser != null) {
			u.setPassword(oldUser.getPassword());
			u.setRole(oldUser.getRole());
			return userRepo.save(u);
		}

		return null;
	}

}
