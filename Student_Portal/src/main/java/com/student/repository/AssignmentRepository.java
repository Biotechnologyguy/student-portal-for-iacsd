package com.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entites.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

	public List<Assignment> findByFaculityId(long id);

}
