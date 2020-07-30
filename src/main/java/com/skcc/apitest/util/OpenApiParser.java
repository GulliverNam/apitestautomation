package com.skcc.apitest.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

public class OpenApiParser {
	
	public static void main(String[] args) throws Exception {
		SwaggerParser parser = new SwaggerParser();
		Swagger model = parser.read("C:\\Users\\Administrator\\Downloads\\swagger1.yaml");
		System.out.println("basePath: "+model.getHost()+ " basePath: "+model.getBasePath());
		Map<String, Path> paths = model.getPaths();
		Set<String> pathSet = paths.keySet();
		for (String path : pathSet) {
			System.out.println("Path: "+path);
			List<Operation> ops = paths.get(path).getOperations();
			for (Operation op : ops) {
				System.out.println(op.getDescription());
			}
		}
	}
	
}
