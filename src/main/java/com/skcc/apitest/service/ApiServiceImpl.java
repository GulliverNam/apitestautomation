package com.skcc.apitest.service;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
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
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;

@Service
public class ApiServiceImpl implements ApiService {
	
	String dir = "C:\\apitest";
	String swaggerDir = dir+"\\swagger\\swagger.json";
	String openApiDir = dir+"\\yaml\\openapi.yaml";
	String collectionDir = dir+"\\collection\\collection.json";
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
					saveOpenAPI(content);
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
			String cmd = "swagger2openapi -y -o "+openApiDir+" "+swaggerDir;
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
		System.out.println(model);
		return model;
	}
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
	
	public void retrievePathsRef(Map<String, Schema> refSchema, Operation method) {
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
							mediaType.setSchema(refSchema.get(refString[refString.length-1]));
						} else if(type == "array") {
							ArraySchema arrSchema = (ArraySchema)schema;
							if(arrSchema.getItems().get$ref() != null) {
								String[] refString = arrSchema.getItems().get$ref().split("/");
								arrSchema.setItems(refSchema.get(refString[refString.length-1]));
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
						mediaType.setSchema(refSchema.get(refString[refString.length-1]));
					} else if(type == "array") {
						ArraySchema arrSchema = (ArraySchema)schema;
						if(arrSchema.getItems().get$ref() != null) {
							String[] refString = arrSchema.getItems().get$ref().split("/");
							arrSchema.setItems(refSchema.get(refString[refString.length-1]));
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
						param.setSchema(refSchema.get(refString[refString.length-1]));
					} else if(type == "array") {
						ArraySchema arrSchema = (ArraySchema)schema;
						if(arrSchema.getItems().get$ref() != null) {
							String[] refString = arrSchema.getItems().get$ref().split("/");
							arrSchema.setItems(refSchema.get(refString[refString.length-1]));
						}
					}
				}
			}
			/**** Parameters ref 수정 끝 ****/
		}
	}
	
	@Override
	public void openAPIToCollection() {
		String cmd = "openapi2postmanv2 -s "+openApiDir+" -o "+collectionDir;
		CLIExecutor.execute(cmd);
		System.out.println("*******OpenAPI converted to Collection*******");
	}

	@Override
	public void addTestScript(Map<String, String> testForm) {
		JsonElement original;
		Gson gson = new Gson();
		try {
			original = gson.fromJson(new FileReader(collectionDir), JsonElement.class);
			addEventToCollection(testForm, original.getAsJsonObject().get("item"), "");
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
		String cmd = "newman run "+collectionDir+" --reporters cli,htmlextra,json-summary --global-var \"baseUrl="+url.toString()+"\"";
		CLIExecutor.execute(cmd);
	}
	
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
	
	public void addEventToCollection(Map<String, String> testForm, JsonElement object, String path) {
		JsonArray arr = object.getAsJsonArray();
		for (JsonElement child : arr) {
			JsonElement item = child.getAsJsonObject().get("item");
			if(item != null)
				addEventToCollection(testForm, item, path + "/" + child.getAsJsonObject().get("name").getAsString());
			else {
				String method = child.getAsJsonObject().get("request").getAsJsonObject().get("method").getAsString().toLowerCase();
				String key = path+"-"+method+"-test";
				String statusCode = testForm.get(key);
				
				TestScriptDTO testScript = new TestScriptDTO();
				testScript.codeCheck(statusCode);
				EventDTO event = new EventDTO();
				event.setScript(testScript);
				JsonElement eventJson = new Gson().toJsonTree(event);
				
				JsonElement target = child.getAsJsonObject().get("event");
				target.getAsJsonArray().add(eventJson);
			}
		}
		return;
	}

}
