package com.skcc.apitest.dto;

import java.util.List;

public class ReqBody {
	private boolean required;
	private Content content;
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "ReqBody [required=" + required + ", content=" + content + "]";
	}
	
	
}
