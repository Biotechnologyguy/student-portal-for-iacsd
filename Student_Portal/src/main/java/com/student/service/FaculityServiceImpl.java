package com.student.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.entites.AssignementAnswer;
import com.student.entites.Assignment;
import com.student.entites.Noticeboard;
import com.student.entites.TimeTable;
import com.student.entites.User;
import com.student.repository.AssignmentAnswerRepository;
import com.student.repository.AssignmentRepository;
import com.student.repository.NoticeboardRepository;
import com.student.repository.TimetableRepository;
import com.student.repository.UserRepository;

@Service
public class FaculityServiceImpl implements FaculityService {

	@Autowired
	private NoticeboardRepository noticeRepo;

	@Autowired
	private AssignmentRepository assignRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AssignmentAnswerRepository answerRepo;

	@Autowired
	private TimetableRepository timetableRepo;

	@Override
	public Noticeboard addNotice(Noticeboard notice) {
		return noticeRepo.save(notice);
	}

	@Override
	public List<Noticeboard> getNoticeByFaculityId(long faculityId) {
		return noticeRepo.findByFaculityId(faculityId);
	}

	@Override
	public List<Noticeboard> getAllNotice() {
		return noticeRepo.findAll();
	}

	@Override
	public Noticeboard getNoticeById(long id) {
		return noticeRepo.findById(id).get();
	}

	@Override
	public Noticeboard updateNoticeboard(Noticeboard notice) {
		return noticeRepo.save(notice);
	}

	@Override
	public boolean deleteNotice(long id) {

		Noticeboard n = noticeRepo.findById(id).get();
		if (n != null) {
			noticeRepo.delete(n);
			return true;
		}
		return false;
	}

	@Override
	public Assignment addAssignment(Assignment assignment) {
		return assignRepo.save(assignment);
	}

	@Override
	public List<Assignment> getAllAssignment() {
		return assignRepo.findAll();
	}

	@Override
	public List<Assignment> getAssignmentByfaculityId(long faculityId) {
		return assignRepo.findByFaculityId(faculityId);
	}

	@Override
	public Assignment getAssignmentById(long id) {
		return assignRepo.findById(id).get();
	}

	@Override
	public Assignment updateAssignment(Assignment assignment) {
		return assignRepo.save(assignment);
	}

	@Override
	public boolean deleteAssignment(long id) {

		Assignment a = assignRepo.findById(id).get();
		if (a != null) {
			assignRepo.delete(a);
			return true;
		}

		return false;
	}

	@Override
	public List<AssignementAnswer> getAllSubmitAnswerByFaculityId(long id) {
		return answerRepo.findByFaculityId(id);
	}

	@Override
	public AssignementAnswer getAssignmentAnswerById(long id) {

		return answerRepo.findById(id).get();
	}

	@Override
	public boolean updateGrade(long aid, String grade) {

		AssignementAnswer as = answerRepo.findById(aid).get();

		if (as != null) {
			as.setGrade(grade);
			as.setId(aid);
			answerRepo.save(as);
			return true;
		}

		return false;
	}

	@Override
	public String checkGrade(long fid, long aid) {

		AssignementAnswer as = answerRepo.findByFaculityIdAndId(fid, aid);

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

	@Override
	public TimeTable addTimeTable(TimeTable timeTable) {
		return timetableRepo.save(timeTable);
	}

	@Override
	public TimeTable getTimetableById(long id) {
		return timetableRepo.findById(id).get();
	}

	@Override
	public List<TimeTable> getAllTimeTableByFaculityId(long id) {
		return timetableRepo.findByFaculityIdOrderByIdDesc(id);
	}

	@Override
	public List<TimeTable> getAllTimeTable() {
		return timetableRepo.findAll();
	}

	@Override
	public boolean deleteTimetable(long id) {

		TimeTable time = timetableRepo.findById(id).get();
		if (time != null) {
			timetableRepo.delete(time);
			return true;
		}
		return false;
	}

	@Override
	public TimeTable updateTimetable(TimeTable timeTable) {
		return timetableRepo.save(timeTable);
	}

}
