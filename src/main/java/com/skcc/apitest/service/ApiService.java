package com.skcc.apitest.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.models.OpenAPI;

public interface ApiService {
	// 구버전(2.0) -> 신버전(3.0)
	public void uploadFile(MultipartFile file);
	public void swaggerToOpenapi(MultipartFile file, String extension);
	public void saveOpenAPI(String doc);
	public OpenAPI correctUrl(OpenAPI model);
	public OpenAPI setReqBody(OpenAPI model, Map<String, Object> reqBodyForm);
	public OpenAPI getApiSpec();
	public void openAPIToCollection();
	public void addTestScript(OpenAPI openAPI, Map<String, String> testForm, Map<String, Object> reqBodyForm);
	public void runTest();
}
