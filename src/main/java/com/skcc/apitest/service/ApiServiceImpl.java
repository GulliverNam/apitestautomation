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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.skcc.apitest.dto.EventDTO;
import com.skcc.apitest.dto.TestScriptDTO;
import com.skcc.apitest.util.CLIExecutor;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;

@Service
public class ApiServiceImpl implements ApiService {
	
	String dir = "C:\\apitest";
	String swaggerDir = dir+"\\swagger\\swagger.";
	String openApiDir = dir+"\\yaml\\openapi.yaml";
	String collectionDir = dir+"\\collection\\collection.json";
	Yaml yamlParser = new Yaml();
	
	/**
	 * 업로드 한 명세서를 파일로 저장 후 Openapi v3.0으로 저장 
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void uploadFile(MultipartFile file) {
		String[] arr = file.getOriginalFilename().split("[.]", 0);
		String extension = arr[arr.length-1];
		try {
			String content = new String(file.getBytes(), StandardCharsets.UTF_8);
			// json 확장자 처리
			if("json".equals(extension)) {
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(content);
				JsonElement swagger = jsonElement.getAsJsonObject().get("swagger");
				JsonElement openapi = jsonElement.getAsJsonObject().get("openapi");
				if(swagger == null && openapi == null) {
					throw new Exception();
				} else if(openapi == null) { // 구버전
					swaggerToOpenapi(file, extension);
				} else if(swagger == null) { // 신버전
					JsonNode jsonNode = new ObjectMapper().readTree(content);
					content = new YAMLMapper().writeValueAsString(jsonNode);
					saveOpenAPI(content);
				}
			}
			// yaml 확장자 처리
			else if("yml".equals(extension) || "yaml".equals(extension)) {
				Map yaml = (Map)yamlParser.load(file.getInputStream());
				String swagger = (String)yaml.get("swagger");
				String openapi = (String)yaml.get("openapi");
				if(swagger == null && openapi == null) {
					throw new Exception();
				} else if(openapi == null || swagger == null) { // 구버전 & 신버전
					swaggerToOpenapi(file, extension);
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
	public void swaggerToOpenapi(MultipartFile file, String extension) {
		// swagger 폴더에 파일 생성
		File outputFile = new File(swaggerDir+extension);
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
			String cmd = "swagger2openapi -y -o "+openApiDir+" "+swaggerDir+extension;
			CLIExecutor.execute(cmd);
			System.out.println("*******swagger converted to OpenAPI*******");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveOpenAPI(String content) {
		File file = new File(openApiDir);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.flush();
			fos.close();
			System.out.println("*******OpenAPI saved*******");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public OpenAPI correctUrl(OpenAPI model) {
		String url = getCorrectUrl(model);
		model.getServers().get(0).setUrl(url);
		return model;
	}
	@Override
	public OpenAPI setReqBody(OpenAPI model, Map<String, Object> reqBodyForm) {
		Set<String> keySet = reqBodyForm.keySet();
		for (String key : keySet) {
			if(!key.contains("content")) {
				String[] keyString = key.split("-");
				String pathName = keyString[0];
				String httpMethod = keyString[1];
				String mediaType = (String) reqBodyForm.get(key);
				PathItem path = model.getPaths().get(pathName);
				Operation op = null;
				if("get".equals(httpMethod)) {
					op = path.getGet();
				} else if("post".equals(httpMethod)) {
					op = path.getPost();
				} else if("put".equals(httpMethod)) {
					op = path.getPut();
				} else if("delete".equals(httpMethod)) {
					op = path.getDelete();
				}
				Schema schema = op.getRequestBody().getContent().get(mediaType).getSchema();
				String type = schema.getType();
				
				Map<String, String> body = new Gson().fromJson((String) reqBodyForm.get(key+"-content"), Map.class);
				
				if("object".equals(type)) {
					Map<String, Schema> props = schema.getProperties();
					Set<String> propsKeys = props.keySet();
					for (String propsKey : propsKeys) {
						Schema prop = props.get(propsKey);
						prop.setDefault(body.get(propsKey));
					}
					schema.setProperties(props);
				} else if("array".equals(type)) {
					
				}
				
			}
		}
		System.out.println("***** Request Body set *****");
		return model;
	}
	
	/**
	 * Openapi 형식의 명세서 정보를 UI에 띄워주기 위한 함수
	 */
	@Override
	public OpenAPI getApiSpec() {
		OpenAPI model = new OpenAPIV3Parser().read(openApiDir);
		Paths paths = model.getPaths();
		Set<String> pathNames = paths.keySet();
		if(model.getComponents() != null) {
			Map<String, Schema> refSchema = model.getComponents().getSchemas();
			retrieveComponentRef(refSchema);
			for (String pathName : pathNames) {
				PathItem path = paths.get(pathName);
				retrievePathsRef(refSchema, path.getGet());
				retrievePathsRef(refSchema, path.getPost());
				retrievePathsRef(refSchema, path.getPut());
				retrievePathsRef(refSchema, path.getDelete());
			}
		}
		System.out.println("***** Get Model *****");
		return model;
	}
	
	
	
	@Override
	public void openAPIToCollection() {
		String cmd = "openapi2postmanv2 -s "+openApiDir+" -o "+collectionDir;
		CLIExecutor.execute(cmd);
		System.out.println("*******OpenAPI converted to Collection*******");
	}

	@Override
	public void addTestScript(OpenAPI openAPI, Map<String, String> testForm, Map<String, Object> reqBodyForm) {
		System.out.println("************** Test Start *************");
		System.out.println(testForm);
		JsonElement original;
		Gson gson = new Gson();
		try {
			original = gson.fromJson(new FileReader(collectionDir), JsonElement.class);
			addEventToCollection(testForm, reqBodyForm, openAPI, original.getAsJsonObject().get("item"), "");
			Writer writer = new FileWriter(collectionDir);
			gson.toJson(original, writer);
			writer.close();
			System.out.println("*********test script added*********");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void runTest() {
		OpenAPI model = new OpenAPIV3Parser().read(openApiDir);
		String url = getCorrectUrl(model);
		System.out.println("url: "+url);
		String cmd = "newman run "+collectionDir+" --reporters htmlextra,json-summary --global-var \"baseUrl="+url.toString()+"\"";
		System.out.println("run test!!!\ncmd: "+cmd);
		CLIExecutor.execute(cmd);
	}
	/**
	 * OpenAPI의 component 부분의 ref 종속성을 없애는 함수
	 * @param refSchemas - component의 schema Map
	 */
	public void retrieveComponentRef(Map<String,Schema> refSchemas) {
		Set<String> schemaKeys = refSchemas.keySet();
		for (String schemaKey : schemaKeys) {
			Schema schema = refSchemas.get(schemaKey);
			String type = schema.getType();
			if("array".equals(type)) {
				ArraySchema arrSchema = (ArraySchema) schema;
				Schema<?> items = arrSchema.getItems();
				if(items.get$ref() != null) {
					String[] refString = items.get$ref().split("/");
					arrSchema.setItems(refSchemas.get(refString[refString.length - 1]));
				}
			}else if("object".equals(type)) {
				Map<String, Schema> props = schema.getProperties();
				Set<String> propsKeys = props.keySet();
				for (String propsKey : propsKeys) {
					Schema prop = props.get(propsKey);
					if(prop.get$ref() != null){
						String[] refString = prop.get$ref().split("/");
						props.put(propsKey, refSchemas.get(refString[refString.length - 1]));
					}
				}
			}
		}
	}
	
	/**
	 * Paths의 ref 종속성을 없애는 함수(Responses, RequestBody, Parameters)
	 * @param refSchemas - component의 schemamap
	 * @param method - Path의 HttpMethod 객체(swagger-parser 객체)
	 */
	public void retrievePathsRef(Map<String, Schema> refSchemas, Operation method) {
		if(method != null) {
			RequestBody reqBody = method.getRequestBody();
			ApiResponses resps = method.getResponses();
			List<Parameter> params = method.getParameters();
			
			/**** Responses ref 수정 ****/
			Set<String> respCodes = resps.keySet();
			for (String code : respCodes) {
				Content content  = resps.get(code).getContent();
				if(content != null) {
					Set<String> mediaNames = content.keySet();
					for (String name : mediaNames) {
						MediaType mediaType = content.get(name);
						Schema schema = mediaType.getSchema();
						String type = schema.getType();
						if(schema.get$ref() != null) {
							String[] refString = schema.get$ref().split("/");
							mediaType.setSchema(refSchemas.get(refString[refString.length-1]));
						} else if(type == "array") {
							ArraySchema arrSchema = (ArraySchema)schema;
							if(arrSchema.getItems().get$ref() != null) {
								String[] refString = arrSchema.getItems().get$ref().split("/");
								arrSchema.setItems(refSchemas.get(refString[refString.length-1]));
							}
						}
					}
				}
				
			}
			/**** Responses ref 수정 끝 ****/
			
			/**** Request Body ref 처리 ****/
			if(reqBody != null) {
				Content content = reqBody.getContent();
				Set<String> mediaNames = content.keySet();
				for (String name : mediaNames) {
					MediaType mediaType = content.get(name);
					Schema schema = mediaType.getSchema();
					String type = schema.getType();
					if(schema.get$ref() != null) {
						String[] refString = schema.get$ref().split("/");
						mediaType.setSchema(refSchemas.get(refString[refString.length-1]));
					} else if(type == "array") {
						ArraySchema arrSchema = (ArraySchema)schema;
						if(arrSchema.getItems().get$ref() != null) {
							String[] refString = arrSchema.getItems().get$ref().split("/");
							arrSchema.setItems(refSchemas.get(refString[refString.length-1]));
						}
					}
				}
			}
			/**** Request Body ref 수정 끝 ****/
			
			/**** Parameters ref 수정 ****/
			if(params != null) {
				for (Parameter param : params) {
					Schema schema = param.getSchema();
					String type = schema.getType();
					if(schema.get$ref() != null) {
						String[] refString = schema.get$ref().split("/");
						param.setSchema(refSchemas.get(refString[refString.length-1]));
					} else if(type == "array") {
						ArraySchema arrSchema = (ArraySchema)schema;
						if(arrSchema.getItems().get$ref() != null) {
							String[] refString = arrSchema.getItems().get$ref().split("/");
							arrSchema.setItems(refSchemas.get(refString[refString.length-1]));
						}
					}
				}
			}
			/**** Parameters ref 수정 끝 ****/
		}
	}
	
	/**
	 * OpenAPI의 올바른 Url을 생성해주는 함수
	 * @param model
	 * @return correct baseUrl
	 */
	public String getCorrectUrl(OpenAPI model) {
		URI uri;
		StringBuilder url = new StringBuilder("http://");
		try {
			uri = new URI(model.getServers().get(0).getUrl());
			url.append(uri.getHost());
			if(uri.getPort() >= 1) url.append(":").append(uri.getPort());
			url.append(uri.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url.toString();
	}
	
	/**
	 * 생성된 Collection에 Test Script를 추가하기 위해 Event 객체를 생성
	 * @param testForm - 입력받은 test 정보
	 * @param object - TestScript를 입력할 최하위 객체(최상위 객체에서 찾을 때 까지 재귀호출)
	 * @param path - 최하위 객체의 path(최상위 객체에서 누적해서 재귀호출)
	 */
	public void addEventToCollection(Map<String, String> testForm, Map<String, Object> reqBodyForm, OpenAPI model, JsonElement object, String path) {
		JsonArray arr = object.getAsJsonArray();
		for (JsonElement child : arr) {
			JsonElement item = child.getAsJsonObject().get("item");
			if(item != null)
				addEventToCollection(testForm, reqBodyForm, model, item, path + "/" + child.getAsJsonObject().get("name").getAsString());
			else {
				String method = child.getAsJsonObject().get("request").getAsJsonObject().get("method").getAsString().toLowerCase();
				String key = path+"-"+method+"-test"; // testForm의 데이터를 찾기 위한 key
				String statusCode = testForm.get(key);
				
				TestScriptDTO testScript = new TestScriptDTO();
				if("default".equals(statusCode)) {
					testScript.getExec().add("pm.test(\"Status code is 404\", function () {pm.response.to.have.status(404);});");
				} else {
					testScript.getExec().add("pm.test(\"Status code is "+statusCode+"\", function () {pm.response.to.have.status("+statusCode+");});");
					if(Integer.parseInt(statusCode) / 100 == 2) {
						Operation op = new Operation();
						if("get".equals(method)) {
							op = model.getPaths().get(path).getGet();
						} else if("post".equals(method)) {
							op = model.getPaths().get(path).getPost();
						} else if("put".equals(method)) {
							op = model.getPaths().get(path).getPut();
						} else if("delete".equals(method)) {
							op = model.getPaths().get(path).getDelete();
						}
						String mediaName = (String)reqBodyForm.get(path+"-"+method+"-requestBody");
						Content content = op.getResponses().get(statusCode).getContent();
						if(content != null) {
							MediaType mediaType = content.get(mediaName);
							MediaType allMediaType = content.get("*/*");
							if(mediaType != null) {
								Schema schema = mediaType.getSchema();
								String type = schema.getType();
								// key check & value type check
								if("object".equals(type)) {
									Map<String, Schema> props = schema.getProperties();
									Set<String> propNames = props.keySet();
									for (String propName : propNames) {
										String propType = props.get(propName).getType();
										testScript.getExec().add("pm.test(\"response key-check: "+ propName + "\", function () {" + 
																 "    pm.expect(pm.response.text()).to.include(\""+propName+"\");" + 
																 "});");
										testScript.getExec().add("pm.test(\"response value-check:"+ propName+"("+propType+")"+"\", function () {" + 
																 "    pm.expect(typeof pm.response.json()."+propName+").to.eql(\""+propType+"\");" + 
																 "});");
										
									}
								} else if("array".equals(type)) {
//									ArraySchema arrSchema = (ArraySchema)schema;
//									Schema<?> items = arrSchema.getItems();
//									items.getType();
								} else {
//									// only value type check
//									String[] baseType = {"integer", "string", "boolean", "number"};
//									if(Arrays.asList(baseType).contains(type)) {
//										
//									}
								}
							} else if(allMediaType != null) {
								
							} else {
								
							}
						}
					}
				}
				
				EventDTO event = new EventDTO();
				event.setScript(testScript);
				JsonElement eventJson = new Gson().toJsonTree(event);
				
				JsonElement target = child.getAsJsonObject().get("event");
				target.getAsJsonArray().add(eventJson);
			}
		}
	}
}
