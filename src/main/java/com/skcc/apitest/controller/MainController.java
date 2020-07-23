package com.skcc.apitest.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.Yaml;

import com.skcc.apitest.util.CLIExecutor;

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
		File file = new File("src/main/resources/yaml/openapi.yaml");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] content = doc.getBytes();
			fos.write(content);
			fos.flush();
			fos.close();
			String dir = System.getProperty("user.dir")+"\\src\\main\\resources";
			String cmd = "openapi2postmanv2 -s "+dir+"\\yaml\\openapi.yaml -o "+dir+"\\collection\\collection.json";
			System.out.println(cmd);
			CLIExecutor.execute(cmd);
			
			cmd = "newman run "+dir+"\\\\collection\\\\collection.json";
			CLIExecutor.execute(cmd);
			System.out.println("finish");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("index");
	}
}
