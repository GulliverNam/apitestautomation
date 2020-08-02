package com.skcc.apitest.dto;

import org.springframework.stereotype.Repository;

@Repository
public class EventDTO {
	
	private String listen;
	private TestScriptDTO script;
	public EventDTO() {
		this.listen = "test";
	}
	public String getListen() {
		return listen;
	}
	public void setListen(String listen) {
		this.listen = listen;
	}
	public TestScriptDTO getScript() {
		return script;
	}
	public void setScript(TestScriptDTO script) {
		this.script = script;
	}
	
	
}
