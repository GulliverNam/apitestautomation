package com.skcc.apitest.service;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Service;

import com.skcc.apitest.util.CLIExecutor;

@Service
public class APIServiceImpl implements APIService {
	
	String dir = "C:\\apitest";
	String yamlDir = dir+"\\yaml\\openapi.yaml";
	String jsonDir = dir+"\\collection\\collection.json";
	
	@Override
	public void saveYaml(String doc) {
		File file = new File(yamlDir);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] content = doc.getBytes();
			fos.write(content);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void yamlToJson() {
		String cmd = "openapi2postmanv2 -s "+yamlDir+" -o "+jsonDir;
		CLIExecutor.execute(cmd);
	}

	@Override
	public void addTestScript() {
		
	}

	@Override
	public void runTest() {
		String cmd = "newman run "+jsonDir+" --global-var \"baseUrl=http://petstore.swagger.io/v1\"";
		CLIExecutor.execute(cmd);
	}

}
