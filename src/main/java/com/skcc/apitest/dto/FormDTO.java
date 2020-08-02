package com.skcc.apitest.dto;

import java.util.Map;

import org.springframework.stereotype.Repository;

import io.swagger.v3.oas.models.OpenAPI;

@Repository
public class FormDTO {
	private OpenAPI paramForm;
	private Map<String, String> testForm;
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
}
