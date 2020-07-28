package com.skcc.apitest.service;

import org.springframework.web.multipart.MultipartFile;

public interface APIService {
	// 구버전(2.0) -> 신버전(3.0)
	public void swaggerToOpenapi(MultipartFile swaggerFile);
	public void saveYaml(String doc);
	public void yamlToJson();
	public void addTestScript();
	public void runTest();
}
