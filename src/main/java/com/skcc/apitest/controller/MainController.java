package com.skcc.apitest.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.Yaml;

@RestController
public class MainController {
	
	@GetMapping("/")
	public ModelAndView main() {
		return new ModelAndView("index");
	}
	
	@PostMapping("/upload")
	public ModelAndView uploadYaml(@RequestBody MultipartFile file) {
		System.out.println(file);
		return new ModelAndView("index");
	}
	
	@PostMapping("/")
	public ModelAndView createYaml(@RequestParam("rowtext") String doc) {
		Yaml yaml = new Yaml();
		Map map = (Map)yaml.load(doc);
		Map paths = (Map) map.get("paths");
		System.out.println(map.get("openapi"));
		System.out.println(map.get("info"));
		System.out.println(map.get("servers"));
		System.out.println(map.get("paths"));
		System.out.println(paths.get("/board"));
		System.out.println(paths.get("/board/{id}"));
		return new ModelAndView("index");
	}
}
