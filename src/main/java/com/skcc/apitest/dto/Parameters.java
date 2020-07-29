package com.skcc.apitest.dto;

public class Parameters {
	private String name;
	private String desc;
	private boolean required;
	private Schema schema;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	@Override
	public String toString() {
		return "Parameters [name=" + name + ", desc=" + desc + ", required=" + required + ", schema=" + schema + "]";
	}
}
