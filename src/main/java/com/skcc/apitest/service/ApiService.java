package com.skcc.apitest.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.models.OpenAPI;

public interface ApiService {
	// 구버전(2.0) -> 신버전(3.0)
	public void uploadFile(MultipartFile file);
	public void swaggerToOpenapi(MultipartFile file);
	public void saveYaml(String doc);
	public OpenAPI getApiSpec();
	public void yamlToCollection();
	public void addTestScript(Map<String, String> testForm);
	public void runTest();
}
