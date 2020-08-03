package com.skcc.apitest.dto;

import java.util.Map;

import org.springframework.stereotype.Repository;

import io.swagger.v3.oas.models.OpenAPI;

@Repository
public class FormDTO {
	private OpenAPI paramForm;
	private Map<String, String> testForm;
	private Map<String, Object> reqBodyForm;
	
	public OpenAPI getParamForm() {
		return paramForm;
	}
	public void setParamForm(OpenAPI paramForm) {
		this.paramForm = paramForm;
	}
	public Map<String, String> getTestForm() {
		return testForm;
	}
	public void setTestForm(Map<String, String> testForm) {
		this.testForm = testForm;
	}
	public Map<String, Object> getReqBodyForm() {
		return reqBodyForm;
	}
	public void setReqBodyForm(Map<String, Object> reqBodyForm) {
		this.reqBodyForm = reqBodyForm;
	}
	
}
