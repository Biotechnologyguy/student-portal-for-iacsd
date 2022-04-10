package com.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entites.Noticeboard;

public interface NoticeboardRepository extends JpaRepository<Noticeboard, Long> {

	public List<Noticeboard> findByFaculityId(long faculityId);

}
