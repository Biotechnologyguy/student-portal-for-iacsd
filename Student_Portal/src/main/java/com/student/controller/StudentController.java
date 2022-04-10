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
import com.student.service.StudentService;

@Controller
@RequestMapping("/student/")
public class StudentController {

	@Autowired
	private FaculityService faculityService;

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StudentService studentService;

	public static int BUFFER_SIZE = 1024 * 100;

	@ModelAttribute
	public void addCommnData(Principal p, Model m) {
		String email = p.getName();
		User user = userRepository.findByEmail(email);
		m.addAttribute("user", user);
	}

	@GetMapping("/")
	public String home() {
		return "student/index";
	}

	@GetMapping("/viewProfile")
	public String viewProfile() {
		return "student/view_profile";
	}

	@GetMapping("/editProfile")
	public String editProfile() {
		return "student/edit_profile";
	}

	@GetMapping("/timetable")
	public String timetable(Model m) {
		List<TimeTable> list = faculityService.getAllTimeTable();
		m.addAttribute("list", list);
		return "student/timetable";
	}

	@GetMapping("/noticeboard")
	public String noticeboard(Model m) {
		List<Noticeboard> list = faculityService.getAllNotice();

		m.addAttribute("title", "view noticeboard");
		m.addAttribute("list", list);
		return "student/noticeboard";
	}

	@GetMapping("/faculity")
	public String faculity(Model m) {
		List<User> list = adminService.getAllFaculity();
		m.addAttribute("title", "View faculity");
		m.addAttribute("list", list);
		return "student/faculity";
	}

	@GetMapping("/assignment")
	public String assignment(Model m) {

		List<Assignment> list = faculityService.getAllAssignment();
		m.addAttribute("title", "Assignment");
		m.addAttribute("list", list);
		m.addAttribute("studentService", studentService);

		return "student/assignment";
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

	@PostMapping("/uploadAssignment")
	public String uploadAssignment(@ModelAttribute AssignementAnswer assignment,
			@RequestParam("filename") MultipartFile file, HttpSession session) {

		assignment.setFileName(file.getOriginalFilename());
		assignment.setGrade("No");
		AssignementAnswer as = studentService.uploadAssignment(assignment);

		if (as != null) {

			try {
				File saveFile = new ClassPathResource("static/assignment_answer").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				// System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			} catch (Exception e) {
				e.printStackTrace();
			}

			session.setAttribute("succMsg", "Assignment Answer upload Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/student/assignment";
	}

	@GetMapping("/loaduploadAnswer/{id}")
	public String loadUploadAssignment(@PathVariable long id, Model m) {

		Assignment as = faculityService.getAssignmentById(id);

		m.addAttribute("assignment", as);

		return "student/upload_assign";
	}

	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute User user, HttpSession session) {

		User u = studentService.updateProfile(user);
		if (u != null) {
			session.setAttribute("succMsg", "Profile Update Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/student/viewProfile";
	}
	@GetMapping("/changePassword")
	public String changePassword() {

		return "student/change_password";
	}

	@PostMapping("/changePsw")
	public String changePasw(Principal p, HttpSession session, @RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) {

		String email = p.getName();
		User currentUser = userRepository.findByEmail(email);

		if (passwordEncoder.matches(oldPassword, currentUser.getPassword())) {

			currentUser.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(currentUser);

			session.setAttribute("succMsg", "Password changed sucessfully");
		} else {
			session.setAttribute("errorMsg", "Old Password is incorrect");
		}

		return "redirect:/student/changePassword";
	}

}
