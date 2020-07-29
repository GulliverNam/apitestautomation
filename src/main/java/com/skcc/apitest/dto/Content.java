package com.skcc.apitest.dto;

public class Content {
	private String mediaType = "application/json";
	private Schema schema;
	
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	@Override
	public String toString() {
		return "Content [mediaType=" + mediaType + ", schema=" + schema + "]";
	}
}
