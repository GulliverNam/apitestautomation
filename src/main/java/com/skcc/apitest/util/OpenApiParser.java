package com.skcc.apitest.util;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.ResolverCache;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.models.RefFormat;

public class OpenApiParser {
	
	public static void main(String[] args) throws Exception {
		OpenAPIV3Parser parser = new OpenAPIV3Parser();
		ParseOptions options = new ParseOptions();
		options.setResolveFully(true);
		OpenAPI model = parser.readLocation("C:\\apitest\\swagger\\swagger.yaml", null, options).getOpenAPI();
//		ResolverCache cache = new ResolverCache(model, null, null);
		
//		System.out.println(cache.loadRef(reqBody.get$ref(), RefFormat.INTERNAL, Object.class));
		System.out.println(model);
	}
	
}
