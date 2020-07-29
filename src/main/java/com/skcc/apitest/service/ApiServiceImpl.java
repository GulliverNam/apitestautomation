package com.skcc.apitest.service;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.skcc.apitest.dto.ApiSpec;
import com.skcc.apitest.dto.Content;
import com.skcc.apitest.dto.Method;
import com.skcc.apitest.dto.Parameters;
import com.skcc.apitest.dto.ReqBody;
import com.skcc.apitest.dto.Schema;
import com.skcc.apitest.util.CLIExecutor;

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
				} else if(openapi == null) {
					swaggerToOpenapi(file);
				} else if(swagger == null) {
					saveYaml(content);
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
	public List<ApiSpec> getApiSpec() {
		//DTO
		List<ApiSpec> specs = new ArrayList<>();
		FileInputStream fis;
		try {
			fis = new FileInputStream(yamlDir);
			Map yaml = yamlParser.loadAs(fis,Map.class);
			Map allPath = (Map)yaml.get("paths");
			Set<String> paths = allPath.keySet();
			for (String path : paths) {
				System.out.println(path+": ");
				Map apiDetails = (Map)allPath.get(path);
				
				//DTO
				ApiSpec spec = new ApiSpec();
				spec.setPath(path);
				List<Method> specMethods = new ArrayList<>();
				
				Set<String> methods = apiDetails.keySet();
				for (String method : methods) {
					System.out.println("\t"+method+": ");
					Map apiDetail = (Map)apiDetails.get(method);
					String summary = (String)apiDetail.get("summary");
					
					//DTO
					Method specMethod = new Method();
					specMethod.setHttpMethod(method);
					specMethod.setSummary(summary);
					List<Parameters> specParams = new ArrayList<>();
					ReqBody specReqBody = new ReqBody();
					
					Set<String> keySet = apiDetail.keySet();
					if(keySet.contains("parameters")) {
						System.out.println("\t\tparameters: ");
						
						
						List<Map> parameters = (List)apiDetail.get("parameters");
						for (Map parameter : parameters) {
							
							String name = (String)parameter.get("name");
							String desc = (String)parameter.get("description");
							boolean required = (boolean)parameter.get("required");
							Map schema = (Map)parameter.get("schema");
							String type = (String)schema.get("type");
							Object defaultVal = schema.get("default");
							
							//DTO
							Parameters specParam = new Parameters();
							Schema specSchema = new Schema();
							specSchema.setType(type);
							specSchema.setDefaultVal(defaultVal);
							specParam.setName(name);
							specParam.setDesc(desc);
							specParam.setRequired(required);
							specParam.setSchema(specSchema);
							specParams.add(specParam);
							
							System.out.println("\t\t\t"+specParam);

						}
					}
					
					if(keySet.contains("requestBody")) {
						System.out.println("\t\trequestBody:");
						Map reqBody = (Map)apiDetail.get("requestBody");
						boolean required = (boolean)reqBody.get("required");
						Map contents = (Map)reqBody.get("content");
						Map content = (Map)contents.get("application/json");
						String type = null;
						Object defaultVal = null;
						if(content != null) {
							Map schema = (Map)content.get("schema");
							type = (String)schema.get("type");
							defaultVal = schema.get("default");
						}
						
						//DTO
						
						Content specContent = new Content();
						Schema specSchema = new Schema();
						specSchema.setType(type);
						specSchema.setDefaultVal(defaultVal);
						specContent.setSchema(specSchema);
						specReqBody.setRequired(required);
						specReqBody.setContent(specContent);
						
						System.out.println("\t\t\t"+specReqBody);
					}
					
					specMethod.setParameters(specParams);
					specMethod.setReqBody(specReqBody);
					specMethods.add(specMethod);
				}
				spec.setMethods(specMethods);
				specs.add(spec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return specs;
	}
	
	/**
	 * Parameter Defualt값 추가 -> Collection 변환 -> TestScript 추가
	 */
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
		FileInputStream fis;
		try {
			fis = new FileInputStream(yamlDir);
			Map yaml = yamlParser.loadAs(fis, Map.class);
			List<Map> servers = (List<Map>)yaml.get("servers");
			URI uri = new URI((String)servers.get(0).get("url"));
			StringBuilder url = new StringBuilder("http://");
			url.append(uri.getHost());
			if(uri.getPort() >= 1) url.append(":").append(uri.getPort());
			url.append(uri.getPath());
			System.out.println("url: "+url);
			String cmd = "newman run "+jsonDir+" --global-var \"baseUrl="+url.toString()+"\"";
			CLIExecutor.execute(cmd);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
