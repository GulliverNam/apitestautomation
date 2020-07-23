package com.skcc.apitest.dto;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class OpenApiV3 {
	
	// layer 1
	private String openapi = "3.0.0";
	private Info info;
	private Servers servers;
	private Paths paths;
	private Component component;
	
	public static class Info{
		String version;
		String title;
	}
	public static class Servers{
		String url;
	}
	public static class Paths{
		String filedPattern;
		List<ApiDetail> apiDetail;
	}
	public static class Component{
		List<Schema> schemas;
		List<Parameters> parameters;
	}
	
	public static class ApiDetail{
		String method;
		RequestBody requestBody;
		List<Responses> responses;
		List<Parameters> parameters;
	}
	
	public static class RequestBody{
		boolean required;
		Content content;
	}
	public static class Responses{
		String statusCode;
		Response response;
	}
	public static class Parameters{
		String name;
		String in;
		boolean required;
		String defaultVal;
	}

	public static class Response{
		Content content;
	}
	
	public static class Content{
		String mediaType;
		Schema schema;
	}
	
	public static class Schema{
		String type;
		Map<String, Object> details;
	}
	
	// Getter & Setter
	public String getOpenapi() {
		return openapi;
	}

	public void setOpenapi(String openapi) {
		this.openapi = openapi;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Servers getServers() {
		return servers;
	}

	public void setServers(Servers servers) {
		this.servers = servers;
	}

	public Paths getPaths() {
		return paths;
	}

	public void setPaths(Paths paths) {
		this.paths = paths;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}
	
}
