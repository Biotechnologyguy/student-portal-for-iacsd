package com.student.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.student.entites.AssignementAnswer;
import com.student.entites.Assignment;
import com.student.entites.Noticeboard;
import com.student.entites.TimeTable;
import com.student.entites.User;
import com.student.repository.UserRepository;
import com.student.service.AdminService;
import com.student.service.FaculityService;

@Controller
@RequestMapping("/faculity/")
public class FaculityController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdminService adminService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private FaculityService faculityService;
	public static int BUFFER_SIZE = 1024 * 100;

	@ModelAttribute
	public void addCommnData(Principal p, Model m) {
		String email = p.getName();
		User user = userRepository.findByEmail(email);
		m.addAttribute("user", user);
	}

	@GetMapping("/")
	public String home() {
		return "faculity/index";
	}

	@GetMapping("/viewProfile")
	public String viewProfile() {
		return "faculity/view_profile";
	}

	@GetMapping("/editProfile")
	public String editProfile() {
		return "faculity/edit_profile";
	}

	@GetMapping("/addTimetable")
	public String addTimeTable() {
		return "faculity/add_timetable";
	}

	@GetMapping("/viewTimetable")
	public String viewTimeTable(Model m, Principal p) {
		String email = p.getName();
		User user = userRepository.findByEmail(email);

		List<TimeTable> list = faculityService.getAllTimeTableByFaculityId(user.getId());
		m.addAttribute("list", list);
		return "faculity/view_timetable";
	}

	@GetMapping("/addNoticeboard")
	public String addNoticeboard() {
		return "faculity/add_noticeboard";
	}

	@GetMapping("/viewNoticeboard")
	public String viewNoticeboard(Principal p, Model m) {

		String email = p.getName();
		User user = userRepository.findByEmail(email);
		List<Noticeboard> list = faculityService.getNoticeByFaculityId(user.getId());
		m.addAttribute("title", "view Noticeboard");
		m.addAttribute("list", list);
		return "faculity/view_noticeboard";
	}

	@GetMapping("/viewStudent")
	public String viewStudent(Model m) {
		List<User> list = adminService.getAllStudent();
		m.addAttribute("title", "View Student");
		m.addAttribute("list", list);
		return "faculity/view_student";
	}

	@GetMapping("/uploadAssignment")
	public String uploadAssignment() {
		return "faculity/upload_assignment";
	}

	@GetMapping("/viewAssignment")
	public String viewAssignment(Model m, Principal p) {
		String email = p.getName();
		User user = userRepository.findByEmail(email);
		List<Assignment> list = faculityService.getAssignmentByfaculityId(user.getId());

		m.addAttribute("title", "view Assignment");
		m.addAttribute("list", list);
		return "faculity/view_assignment";
	}

	@PostMapping("/createNotice")
	public String createNoticeboard(@ModelAttribute Noticeboard notice, HttpSession session) {

		Noticeboard n = faculityService.addNotice(notice);
		if (n != null) {
			session.setAttribute("succMsg", "Notice Added Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		return "redirect:/faculity/addNoticeboard";
	}

	@GetMapping("/editNoticeboard/{id}")
	public String editNoticeboard(@PathVariable(name = "id") long id, Model m) {

		Noticeboard n = faculityService.getNoticeById(id);

		m.addAttribute("title", "Edit Noticeboard");
		m.addAttribute("notice", n);

		return "faculity/edit_noticeboard";
	}

	@PostMapping("/updateNotice")
	public String updateNoticeboard(@ModelAttribute Noticeboard notice, HttpSession session) {

		Noticeboard n = faculityService.addNotice(notice);
		if (n != null) {
			session.setAttribute("succMsg", "Notice update Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		return "redirect:/faculity/viewNoticeboard";
	}

	@GetMapping("/deleteNotice/{id}")
	public String deleteNoticeboard(@PathVariable long id, HttpSession session) {

		Boolean f = faculityService.deleteNotice(id);
		if (f) {
			session.setAttribute("succMsg", "Notice delete Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/faculity/viewNoticeboard";
	}

	@PostMapping("/uploadAssignmentDb")
	public String uploadAssignment(@ModelAttribute Assignment assignment, @RequestParam("filename") MultipartFile file,
			HttpSession session) {

		assignment.setFileName(file.getOriginalFilename());

		Assignment as = faculityService.addAssignment(assignment);

		if (as != null) {

			try {
				File saveFile = new ClassPathResource("static/assignment").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				// System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e) {
				e.printStackTrace();
			}

			session.setAttribute("succMsg", "Assignment upload Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/faculity/uploadAssignment";
	}

	@GetMapping("/download/{id}")
	public void downloadAssignment(@PathVariable long id, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		Assignment as = faculityService.getAssignmentById(id);
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			File saveFile = new ClassPathResource("static/assignment").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + as.getFileName());

			File file = new File(path + "");

			if (file.exists()) {

				/**** Setting The Content Attributes For The Response Object ****/

				String mimeType = "application/octet-stream";
				response.setContentType(mimeType);

				/**** Setting The Headers For The Response Object ****/

				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
				response.setHeader(headerKey, headerValue);

				/**** Get The Output Stream Of The Response ****/

				outStream = response.getOutputStream();
				inputStream = new FileInputStream(file);
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				/****
				 * Write Each Byte Of Data Read From The Input Stream Write Each Byte Of Data
				 * Read From The Input Stream Into The Output Stream
				 ***/
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
			}
		} catch (IOException ioExObj) {
			System.out.println("Exception While Performing The I/O Operation?= " + ioExObj.getMessage());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}

			outStream.flush();
			if (outStream != null) {
				outStream.close();
			}
		}

	}

	@GetMapping("/editAssignment/{id}")
	public String editAssignment(@PathVariable long id, Model m) {
		Assignment as = faculityService.getAssignmentById(id);
		m.addAttribute("title", "Edit Assignment");
		m.addAttribute("assignment", as);
		return "faculity/edit_assignment";
	}

	@PostMapping("/updateAssignment")
	public String updateAssignment(@ModelAttribute Assignment assignment, @RequestParam("filename") MultipartFile file,
			HttpSession session) {

		Assignment oldAssignment = faculityService.getAssignmentById(assignment.getId());

		try {

			if (!file.isEmpty()) {

				File deletefile = new ClassPathResource("static/assignment").getFile();
				File f = new File(deletefile, oldAssignment.getFileName());
				f.delete();

				// update new photo
				File saveFile = new ClassPathResource("static/assignment").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				assignment.setFileName(file.getOriginalFilename());
			} else {
				assignment.setFileName(oldAssignment.getFileName());
			}

			Assignment updateAssignment = faculityService.updateAssignment(assignment);

			if (updateAssignment != null) {
				session.setAttribute("succMsg", "Assignment update Sucessfully");

			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/faculity/viewAssignment";
	}

	@GetMapping("/deleteAssignment/{id}")
	public String deleteAssignment(@PathVariable long id, HttpSession session) {

		Boolean f = faculityService.deleteAssignment(id);
		if (f) {
			session.setAttribute("succMsg", "Assignment Delete Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/faculity/viewAssignment";
	}

	@GetMapping("/viewAssignmentAnswer")
	public String viewAssignmentAnswer(Model m, Principal p) {
		String email = p.getName();
		User user = userRepository.findByEmail(email);
		List<AssignementAnswer> list = faculityService.getAllSubmitAnswerByFaculityId(user.getId());
		m.addAttribute("title", "view Assignment Answer");
		m.addAttribute("list", list);
		m.addAttribute("faculityService", faculityService);
		return "faculity/assignment_answer";
	}

	@GetMapping("/downloadAnswer/{id}")
	public void downloadAssignmentAnswer(@PathVariable long id, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		AssignementAnswer as = faculityService.getAssignmentAnswerById(id);
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			File saveFile = new ClassPathResource("static/assignment_answer").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + as.getFileName());

			File file = new File(path + "");

			if (file.exists()) {

				/**** Setting The Content Attributes For The Response Object ****/

				String mimeType = "application/octet-stream";
				response.setContentType(mimeType);

				/**** Setting The Headers For The Response Object ****/

				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
				response.setHeader(headerKey, headerValue);

				/**** Get The Output Stream Of The Response ****/

				outStream = response.getOutputStream();
				inputStream = new FileInputStream(file);
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				/****
				 * Write Each Byte Of Data Read From The Input Stream Write Each Byte Of Data
				 * Read From The Input Stream Into The Output Stream
				 ***/
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
			}
		} catch (IOException ioExObj) {
			System.out.println("Exception While Performing The I/O Operation?= " + ioExObj.getMessage());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}

			outStream.flush();
			if (outStream != null) {
				outStream.close();
			}
		}

	}

	@GetMapping("/lgradeAssignment/{id}")
	public String loadGradeAssignment(@PathVariable long id, Model m) {
		AssignementAnswer as = faculityService.getAssignmentAnswerById(id);

		m.addAttribute("title", "view grade assignment");
		m.addAttribute("answer", as);

		return "faculity/grade_assignment";
	}

	@GetMapping("/grade")
	public String grade(@RequestParam("aid") long aid, @RequestParam("grade") String grade, HttpSession session) {

		boolean f = faculityService.updateGrade(aid, grade);
		if (f) {
			session.setAttribute("succMsg", "Graded Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		return "redirect:/faculity/viewAssignmentAnswer";
	}

	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute User user, HttpSession session) {

		User u = faculityService.updateProfile(user);
		if (u != null) {
			session.setAttribute("succMsg", "Profile Update Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/faculity/viewProfile";
	}

	@PostMapping("/createTimeTable")
	public String addTimeTable(@ModelAttribute TimeTable time, HttpSession session) {

		TimeTable t = faculityService.addTimeTable(time);
		if (t != null) {
			session.setAttribute("succMsg", "Added Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/faculity/addTimetable";
	}

	@GetMapping("/editTimetable/{id}")
	public String loadEditTimeTable(@PathVariable long id, Model m) {

		TimeTable t = faculityService.getTimetableById(id);

		m.addAttribute("title", "Edit Time Table");
		m.addAttribute("time", t);

		return "faculity/edit_timetable";
	}

	@PostMapping("/updateTimeTable")
	public String updateTimeTable(@ModelAttribute TimeTable time, HttpSession session) {

		TimeTable t = faculityService.updateTimetable(time);
		if (t != null) {
			session.setAttribute("succMsg", "Timetable update sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		return "redirect:/faculity/viewTimetable";
	}

	@GetMapping("/deleteTimetable/{id}")
	public String deleteTimetable(@PathVariable long id, HttpSession session) {
		boolean f = faculityService.deleteTimetable(id);
		if (f) {
			session.setAttribute("succMsg", "Delete timetable sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		return "redirect:/faculity/viewTimetable";
	}

	@GetMapping("/changePassword")
	public String changePassword() {

		return "faculity/change_password";
	}

	@PostMapping("/changePsw")
	public String changePasw(Principal p, HttpSession session, @RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) {

		String email = p.getName();
		User currentUser = userRepository.findByEmail(email);

		if (passwordEncoder.matches(oldPassword, currentUser.getPassword())) {

			currentUser.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(currentUser);

			session.setAttribute("succMsg", "password change sucessfully");
		} else {
			session.setAttribute("errorMsg", "old password is incorrect");
		}

		return "redirect:/faculity/changePassword";
	}

}
