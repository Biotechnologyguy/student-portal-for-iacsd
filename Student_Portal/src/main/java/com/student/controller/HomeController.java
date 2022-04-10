package com.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home Page");
		return "index";
	}

	@GetMapping("/signin")
	public String login(Model m) {
		m.addAttribute("title", "Login");
		return "login";
	}

	@GetMapping("/register")
	public String register(Model m) {
		m.addAttribute("title", "Register");
		return "register";
	}

}
