package com.skcc.apitest.service;

import java.io.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.skcc.apitest.util.CLIExecutor;

@Service
public class APIServiceImpl implements APIService {
	
	String dir = "C:\\apitest";
	String swaggerDir = dir+"\\swagger\\swagger.json";
	String yamlDir = dir+"\\yaml\\openapi.yaml";
	String jsonDir = dir+"\\collection\\collection.json";
	
	@Override
	public void swaggerToOpenapi(MultipartFile swaggerFile) {
		File file = new File(swaggerDir);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(swaggerFile.getBytes());
			fos.flush();
			fos.close();
			String cmd = "swagger2openapi -y -o "+yamlDir+" "+swaggerDir;
			CLIExecutor.execute(cmd);
			System.out.println("*******swagger converted to yaml*******");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
		System.out.println("*******yaml converted to Json*******");
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
