package com.skcc.apitest.dto;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class ApiSpec {
	private String path;
	private List<Method> methods;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	@Override
	public String toString() {
		return "ApiSpec [path=" + path + ", methods=" + methods + "]";
	}
	
	
//	public class Methods{
//		public String description;
//		public ReqParam reqParam;
//		public List<Parameters> parameters;
//		public List<Responses> responses;
//	}
//	
//	public class Parameters {
//		public String name;
//		public String desc;
//		public boolean required;
//		public Schema schema;
//	}
//	public class ReqParam {
//		public boolean required;
//		public Content content;
//	}
//	public class Responses {
//		public String code;
//		public Content content;
//	}
//	
//	public class Schema{
//		public String type;
//		public String defaultVal;
//	}
//	
//	public class Content{
//		public String mediaType;
//		public Schema schema;
//	}
}
