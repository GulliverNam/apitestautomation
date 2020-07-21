package com.skcc.apitest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {
	
	@GetMapping("/")
	public ModelAndView main() {
		return new ModelAndView("index");
	}
	
	@PostMapping("/")
	public ModelAndView createYaml() {
		return new ModelAndView("index");
	}
}
