package com.skcc.apitest.service;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.skcc.apitest.util.CLIExecutor;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;

@Service
public class ApiServiceImpl implements ApiService {
	
	String dir = "C:\\apitest";
	String swaggerDir = dir+"\\swagger\\swagger.json";
	String yamlDir = dir+"\\yaml\\openapi.yaml";
	String jsonDir = dir+"\\collection\\collection.json";
	Yaml yamlParser = new Yaml();
	@Override
	@SuppressWarnings("deprecation")
	public void uploadFile(MultipartFile file) {
		String[] arr = file.getOriginalFilename().split("[.]", 0);
		String extension = arr[arr.length-1];
		try {
			String content = new String(file.getBytes(), StandardCharsets.UTF_8);
			if("json".equals(extension)) {
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(content);
				JsonElement swagger = jsonElement.getAsJsonObject().get("swagger");
				JsonElement openapi = jsonElement.getAsJsonObject().get("openapi");
				if(swagger == null && openapi == null) {
					throw new Exception();
				} else if(openapi == null) {
					swaggerToOpenapi(file);
				} else if(swagger == null) {
					JsonNode jsonNode = new ObjectMapper().readTree(content);
					content = new YAMLMapper().writeValueAsString(jsonNode);
					saveYaml(content);
				}
			}
			else if("yml".equals(extension) || "yaml".equals(extension)) {
				Map yaml = (Map)yamlParser.load(file.getInputStream());
				String swagger = (String)yaml.get("swagger");
				String openapi = (String)yaml.get("openapi");
				if(swagger == null && openapi == null) {
					throw new Exception();
				} else if(openapi == null || swagger == null) {
					swaggerToOpenapi(file);
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * swagger 명세서 구버전(2.0)을 신버전(3.0)으로 변환하는 함수
	 */
	@Override
	public void swaggerToOpenapi(MultipartFile file) {
		File outputFile = new File(swaggerDir);
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
			String cmd = "swagger2openapi -y -o "+yamlDir+" "+swaggerDir;
			CLIExecutor.execute(cmd);
			System.out.println("*******swagger converted to Openapi*******");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveYaml(String content) {
		File file = new File(yamlDir);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.flush();
			fos.close();
			System.out.println("*******yaml saved*******");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public OpenAPI getApiSpec() {
		OpenAPI model = new OpenAPIV3Parser().read(yamlDir);
		return model;
	}
	
	@Override
	public void yamlToCollection() {
		String cmd = "openapi2postmanv2 -s "+yamlDir+" -o "+jsonDir;
		CLIExecutor.execute(cmd);
		System.out.println("*******yaml converted to Json*******");
	}

	@Override
	public void addTestScript() {
		
	}

	@Override
	public void runTest() {
		OpenAPI model = new OpenAPIV3Parser().read(yamlDir);
		URI uri;
		try {
			uri = new URI(model.getServers().get(0).getUrl());
			StringBuilder url = new StringBuilder("http://");
			url.append(uri.getHost());
			if(uri.getPort() >= 1) url.append(":").append(uri.getPort());
			url.append(uri.getPath());
			System.out.println("url: "+url);
			String cmd = "newman run "+jsonDir+" --global-var \"baseUrl="+url.toString()+"\"";
			CLIExecutor.execute(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
