package com.skcc.apitest.util;


import java.awt.Component;
import java.sql.Ref;
import java.util.Map;
import java.util.Set;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.converter.SwaggerConverter;
import io.swagger.v3.parser.util.RefUtils;

public class OpenApiParser {
	
	public static void main(String[] args) throws Exception {
		OpenAPIV3Parser parser = new OpenAPIV3Parser();
		OpenAPI model = parser.read("C:\\apitest\\yaml\\swagger-editor_sample.yaml");
		Content content = model.getPaths().get("/pet").getPut().getRequestBody().getContent();
		Map<String, Schema> refSchema = model.getComponents().getSchemas();
		Set<String> types = content.keySet();
		for (String type : types) {
			String[] refString = content.get(type).getSchema().get$ref().split("/");
			String ref = refString[refString.length-1];
			System.out.println("*********ref**********");
			System.out.println(refSchema.get(ref));
		}
	}
	
}
