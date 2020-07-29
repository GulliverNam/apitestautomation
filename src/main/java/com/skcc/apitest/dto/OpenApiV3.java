package com.skcc.apitest.dto;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class OpenApiV3 {
	
	// layer 1
	public String openapi = "3.0.0";
	public Info info;
	public Servers servers;
	public Paths paths;
	public Component component;
	
	/** Info **/
	public class Info{
		String version;
		String title;
		
		@Override
		public String toString() {
			return "Info [version=" + version + ", title=" + title + "]";
		}
		
	}
	/**********/
	
	/** Servers **/
	public class Servers{
		String url;

		@Override
		public String toString() {
			return "Servers [url=" + url + "]";
		}
	}
	/************/
	
	
	/** Paths **/
	// layer 2
	public class Paths{
		String fieldPattern;
		List<ApiDetail> apiDetail;
		@Override
		public String toString() {
			return "Paths [fieldPattern=" + fieldPattern + ", apiDetail=" + apiDetail + "]";
		}
	}
	
	// layer 3
	public class ApiDetail{
		String method;
		RequestBody requestBody;
		List<Responses> responses;
		List<Parameters> parameters;
		@Override
		public String toString() {
			return "ApiDetail [method=" + method + ", requestBody=" + requestBody + ", responses=" + responses
					+ ", parameters=" + parameters + "]";
		}
	}
	
	//layer 4
	public class RequestBody{
		boolean required;
		Content content;
		@Override
		public String toString() {
			return "RequestBody [required=" + required + ", content=" + content + "]";
		}
		
	}
	public class Responses{
		String statusCode;
		Response response;
		@Override
		public String toString() {
			return "Responses [statusCode=" + statusCode + ", response=" + response + "]";
		}
		
	}
	public class Parameters{
		String name;
		String in;
		boolean required;
		String defaultVal;
		@Override
		public String toString() {
			return "Parameters [name=" + name + ", in=" + in + ", required=" + required + ", defaultVal=" + defaultVal
					+ "]";
		}
	}

	// layer 5
	public class Content{
		String mediaType;
		Schema schema;
		@Override
		public String toString() {
			return "Content [mediaType=" + mediaType + ", schema=" + schema + "]";
		}
	}
	
	public class Response{
		Content content;

		@Override
		public String toString() {
			return "Response [content=" + content + "]";
		}
		
	}
	
	// layer 6
	public class Schema{
		String type;
		Map<String, Object> details;
		@Override
		public String toString() {
			return "Schema [type=" + type + ", details=" + details + "]";
		}
		
	}
	/***********/
	
	/** Component **/
	public class Component{
		List<Schema> schemas;
		List<Parameters> parameters;
		@Override
		public String toString() {
			return "Component [schemas=" + schemas + ", parameters=" + parameters + "]";
		}
		
	}
	/**************/
	
}
