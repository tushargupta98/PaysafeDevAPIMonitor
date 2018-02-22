package com.paysafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot main Application
 *
 */
@SpringBootApplication
@RestController
public class Application extends SpringBootServletInitializer{

	public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
	
    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}
    
    @RequestMapping(value="/")
    public String welcome(){
    	return "API Monitoring APP!";
    }
}
