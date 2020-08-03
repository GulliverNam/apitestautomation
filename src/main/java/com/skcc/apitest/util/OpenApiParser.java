package com.skcc.apitest.util;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.util.RefUtils;

public class OpenApiParser {
	
	public static void main(String[] args) throws Exception {
		OpenAPI model = new OpenAPIV3Parser().read("C:\\apitest\\yaml\\openapi_3.0.yaml");
		String ref = model.getPaths().get("/api/v1/integration/linked/projects").getGet().getResponses().get("200").get$ref();
		System.out.println(ref);
//		RefUtils.computeRefFormat();
	}
	
}
