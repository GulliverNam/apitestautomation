package com.skcc.apitest.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import com.skcc.apitest.dto.ApiSpec;
import com.skcc.apitest.service.ApiService;
import com.skcc.apitest.util.CLIExecutor;

@RestController
public class MainController {
	
	@Autowired
	ApiService service;
	
	@GetMapping("/")
	public ModelAndView mainPage() {
		return new ModelAndView("swagger");
	}
	
	@PostMapping("/upload")
	public List<ApiSpec> upload(@RequestParam("apidoc") MultipartFile file) {
		service.uploadFile(file);
		List<ApiSpec> apiSpec = service.getApiSpec();
		System.out.println(apiSpec);
		return apiSpec;
	}
	
}
