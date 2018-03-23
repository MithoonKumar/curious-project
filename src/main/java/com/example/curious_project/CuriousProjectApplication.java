package com.example.curious_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class CuriousProjectApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CuriousProjectApplication.class);
		Properties properties = new Properties();
		properties.setProperty("spring.resources.staticLocations", "classpath:/web/frontend/, classpath:/static/");
		app.setDefaultProperties(properties);
		app.run(args);
	}
}
