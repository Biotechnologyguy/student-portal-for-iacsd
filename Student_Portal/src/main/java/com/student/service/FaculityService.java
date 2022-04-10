package com.student.service;

import java.util.List;

import com.student.entites.AssignementAnswer;
import com.student.entites.Assignment;
import com.student.entites.Noticeboard;
import com.student.entites.TimeTable;
import com.student.entites.User;

public interface FaculityService {

	public Noticeboard addNotice(Noticeboard notice);

	public List<Noticeboard> getAllNotice();

	public List<Noticeboard> getNoticeByFaculityId(long faculityId);

	public Noticeboard getNoticeById(long id);

	public Noticeboard updateNoticeboard(Noticeboard notice);

	public boolean deleteNotice(long id);

	public Assignment addAssignment(Assignment assignment);

	public List<Assignment> getAllAssignment();

	public List<Assignment> getAssignmentByfaculityId(long faculityId);

	public Assignment getAssignmentById(long id);

	public Assignment updateAssignment(Assignment assignment);

	public boolean deleteAssignment(long id);

	public List<AssignementAnswer> getAllSubmitAnswerByFaculityId(long id);

	public AssignementAnswer getAssignmentAnswerById(long id);

	public boolean updateGrade(long aid, String grade);

	public String checkGrade(long fid, long aid);

	public User updateProfile(User u);

	
	public TimeTable addTimeTable(TimeTable timeTable);

	public TimeTable getTimetableById(long id);

	public List<TimeTable> getAllTimeTableByFaculityId(long id);

	public List<TimeTable> getAllTimeTable();

	public boolean deleteTimetable(long id);

	public TimeTable updateTimetable(TimeTable timeTable);

}
