package com.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entites.AssignementAnswer;

public interface AssignmentAnswerRepository extends JpaRepository<AssignementAnswer, Long> {

	public AssignementAnswer findByUserIdAndAssignmentId(long uid, long aid);

	public List<AssignementAnswer> findByFaculityId(long id);
	
	public AssignementAnswer findByFaculityIdAndId(long uid, long aid);
	
	public boolean existsByUserIdAndAssignmentId(long uid,long aid);

}
