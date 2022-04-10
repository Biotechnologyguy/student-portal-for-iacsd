package com.student.service;

import com.student.entites.AssignementAnswer;
import com.student.entites.User;

public interface StudentService {
	
	public AssignementAnswer uploadAssignment(AssignementAnswer answer);
	
	public boolean checkUploadAnswerByUser(long userId,long assignmentId);
	
	public String checkGradedByUser(long userId,long assignmentId);
	
	public User updateProfile(User u);

}
