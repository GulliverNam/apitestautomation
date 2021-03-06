package com.skcc.apitest.controller;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.skcc.apitest.dto.FormDTO;
import com.skcc.apitest.service.ApiService;

import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;

@RestController
public class MainController {
	
	@Autowired
	ApiService service;
	
	@GetMapping("/")
	public ModelAndView mainPage() {
		return new ModelAndView("swagger");
	}
	
	@RequestMapping("/debug")
	public String debug(@RequestBody OpenAPI model) {
		System.out.println("debug!!");
		System.out.println(model.getServers());
		
		Map<String, String> returnJson = new HashMap<>();
		returnJson.put("msg", "success");
		
		return new Gson().toJson(returnJson);
	}
	
	@PostMapping("/upload")
	public OpenAPI upload(@RequestParam("apidoc") MultipartFile file) {
		service.uploadFile(file);
		OpenAPI model = service.getApiSpec();
		return model;
	}
	
	@PostMapping("/test")
	public String test(@RequestBody FormDTO form) {
		OpenAPI openAPI = form.getOpenAPI();
		System.out.println("****request body form****");
		System.out.println(form.getReqBodyForm());
		try {
			openAPI = service.correctUrl(openAPI);
			openAPI = service.setReqBody(openAPI, form.getReqBodyForm());
			service.saveOpenAPI(Yaml.pretty().writeValueAsString(openAPI));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		service.openAPIToCollection();
		
		service.addTestScript(openAPI, form.getTestForm(), form.getReqBodyForm());
		
		service.runTest();
		Map<String, String> returnJson = new HashMap<>();
		returnJson.put("msg", "success");
		
		return new Gson().toJson(returnJson);
	}
	
}
