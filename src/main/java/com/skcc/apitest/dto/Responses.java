package com.skcc.apitest.dto;

public class Responses {
	private String code;
	private Content content;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Responses [code=" + code + ", content=" + content + "]";
	}
}
