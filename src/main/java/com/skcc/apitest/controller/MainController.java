package com.skcc.apitest.controller;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.skcc.apitest.service.ApiService;

import io.swagger.v3.oas.models.OpenAPI;

@RestController
public class MainController {
	
	@Autowired
	ApiService service;
	
	@GetMapping("/")
	public ModelAndView mainPage() {
		return new ModelAndView("swagger");
	}
	
	@PostMapping("/upload")
	public OpenAPI upload(@RequestParam("apidoc") MultipartFile file) {
		service.uploadFile(file);
		OpenAPI model = service.getApiSpec();
//		service.yamlToCollection();
//		service.runTest();
		return model;
	}
	
}
