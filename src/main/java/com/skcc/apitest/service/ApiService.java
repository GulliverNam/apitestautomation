package com.skcc.apitest.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.skcc.apitest.dto.ApiSpec;

public interface ApiService {
	// 구버전(2.0) -> 신버전(3.0)
	public void uploadFile(MultipartFile file);
	public void swaggerToOpenapi(MultipartFile file);
	public void saveYaml(String doc);
	public List<ApiSpec> getApiSpec();
	public void yamlToCollection();
	public void addTestScript();
	public void runTest();
}
