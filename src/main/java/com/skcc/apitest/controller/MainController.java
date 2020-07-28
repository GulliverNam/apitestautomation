package com.skcc.apitest.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.yaml.snakeyaml.Yaml;

import com.skcc.apitest.service.APIService;
import com.skcc.apitest.util.CLIExecutor;

@RestController
public class MainController {
	
	@Autowired
	APIService service;
	
//	@GetMapping("/")
//	public ModelAndView main() {
//		return new ModelAndView("index");
//	}
//	@PostMapping("/upload")
//	public ModelAndView uploadYaml(@RequestParam("apidoc") MultipartFile file) {
//		System.out.println(file);
//		return new ModelAndView("index");
//	}
	
	@GetMapping("/")
	public ModelAndView swaggerMain() {
		return new ModelAndView("swagger");
	}
	
	
	@PostMapping("/upload")
	public RedirectView uploadSwagger(@RequestParam("apidoc") MultipartFile file) {
		service.swaggerToOpenapi(file);
		service.yamlToJson();
		service.addTestScript();
		service.runTest();
		return new RedirectView("/");
	}
	
}
