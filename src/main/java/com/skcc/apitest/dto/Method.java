package com.skcc.apitest.dto;

import java.util.List;

public class Method {
	private String httpMethod;
	private String summary;
	private ReqBody reqBody;
	private List<Parameters> parameters;
//	private List<Responses> responses;
	
	
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public ReqBody getReqBody() {
		return reqBody;
	}
	public void setReqBody(ReqBody reqBody) {
		this.reqBody = reqBody;
	}
	
	public List<Parameters> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameters> parameters) {
		this.parameters = parameters;
	}
	@Override
	public String toString() {
		return "Method [httpMethod=" + httpMethod + ", summary=" + summary + ", reqBody=" + reqBody + ", parameters="
				+ parameters + "]";
	}
	
//	public List<Responses> getResponses() {
//		return responses;
//	}
//	public void setResponses(List<Responses> responses) {
//		this.responses = responses;
//	}
//	@Override
//	public String toString() {
//		return "Methods [description=" + description + ", reqParam=" + reqParam + ", parameters=" + parameters
//				+ ", responses=" + responses + "]";
//	}
}
