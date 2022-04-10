package com.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entites.TimeTable;

public interface TimetableRepository extends JpaRepository<TimeTable, Long> {

	public List<TimeTable> findByFaculityIdOrderByIdDesc(long id);

}
