package com.skcc.apitest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class TestScriptDTO {
	/*
	"listen": "test",
	"script": {
		"id": "a4d88bd2-8dcd-4dbc-a28a-f3e19ef624f3",
		"exec": [
			"pm.test(\"Response time is less than 200ms\", function () {\r",
			"    pm.expect(pm.response.responseTime).to.be.below(200);\r",
			"});"
		],
		"type": "text/javascript"
	}
	*/
	private String id;
	private List<String> exec;
	private String type;
	
	public TestScriptDTO() {
		this.id = UUID.randomUUID().toString();
		this.exec = new ArrayList<String>();
		this.type = "text/javascript";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getExec() {
		return exec;
	}
	public void setExec(List<String> exec) {
		this.exec = exec;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void codeCheck(String statusCode) {
		this.exec.add("pm.test(\"Status code is "+statusCode+"\", function () {pm.response.to.have.status("+statusCode+");});");
	}
}
