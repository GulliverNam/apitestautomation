package com.skcc.apitest.dto;

public class Schema {
	private String type;
	private Object defaultVal;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getDefaultVal() {
		return defaultVal;
	}
	public void setDefaultVal(Object defaultVal) {
		this.defaultVal = defaultVal;
	}
	@Override
	public String toString() {
		return "Schema [type=" + type + ", defaultVal=" + defaultVal + "]";
	}
	
	
}
