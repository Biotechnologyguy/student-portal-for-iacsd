package com.student.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.student.entites.User;
import com.student.service.AdminService;

@Controller
@RequestMapping("/admin/")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@GetMapping("/")
	public String home() {
		return "admin/index";
	}

	@GetMapping("/addStudent")
	public String addStudent() {
		return "admin/add_student";
	}

	@GetMapping("/editStudent/{id}")
	public String editStudent(@PathVariable(name = "id") long id, Model m) {
		User s = adminService.getUserById(id);
		m.addAttribute("title", "Edit Student");
		m.addAttribute("student", s);
		return "admin/edit_student";
	}

	@GetMapping("/viewStudent")
	public String viewStudent(Model m) {
		List<User> list = adminService.getAllStudent();
		m.addAttribute("title", "View Student");
		m.addAttribute("list", list);
		return "admin/view_student";
	}

	@PostMapping("/createStudent")
	public String registerStudent(@ModelAttribute User user, HttpSession session) {

		user.setPassword(passwordEncode.encode(user.getPassword()));

		if (adminService.checkEmail(user.getEmail())) {
			session.setAttribute("errorMsg", "Email already exists");
		} else {
			user.setRole("ROLE_USER");
			User u = adminService.addUser(user);
			if (u != null) {
				session.setAttribute("succMsg", "Register Sucessfully");
			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}
		}
		return "redirect:/admin/addStudent";
	}

	@PostMapping("/updateStudent")
	public String updateStudent(@ModelAttribute User user, HttpSession session) {

		User updateUser = adminService.updateUser(user);
		if (updateUser != null) {
			session.setAttribute("succMsg", "Update Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		return "redirect:/admin/viewStudent";
	}

	@GetMapping("/deleteStudent/{id}")
	public String deleteStudent(@PathVariable long id, HttpSession session) {

		if (adminService.deleteUser(id)) {
			session.setAttribute("succMsg", "Delete Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		return "redirect:/admin/viewStudent";
	}

	@GetMapping("/addFaculity")
	public String addFaculity() {
		return "admin/add_faculity";
	}

	@PostMapping("/createFaculity")
	public String registerFaculity(@ModelAttribute User user, HttpSession session) {

		user.setPassword(passwordEncode.encode(user.getPassword()));

		if (adminService.checkEmail(user.getEmail())) {
			session.setAttribute("errorMsg", "Email already exists");
		} else {
			user.setRole("ROLE_FACULITY");
			User u = adminService.addUser(user);
			if (u != null) {
				session.setAttribute("succMsg", "Register Sucessfully");
			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}
		}
		return "redirect:/admin/addFaculity";
	}

	@GetMapping("/editFaculity/{id}")
	public String editFaculity(@PathVariable long id, Model m) {
		User u = adminService.getUserById(id);
		m.addAttribute("faculity", u);
		return "admin/edit_faculity";
	}

	@GetMapping("/viewFaculity")
	public String viewFaculity(Model m) {
		List<User> list = adminService.getAllFaculity();
		m.addAttribute("title", "View faculity");
		m.addAttribute("list", list);
		return "admin/view_faculity";
	}
	
	@GetMapping("/deleteFaculity/{id}")
	public String deleteFaculity(@PathVariable long id, HttpSession session) {

		if (adminService.deleteUser(id)) {
			session.setAttribute("succMsg", "Delete Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		return "redirect:/admin/viewFaculity";
	}

	
	@PostMapping("/updateFaculity")
	public String updateFaculity(@ModelAttribute User user, HttpSession session) {

		User updateUser = adminService.updateUser(user);
		if (updateUser != null) {
			session.setAttribute("succMsg", "Update Sucessfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}
		return "redirect:/admin/viewFaculity";
	}
}
