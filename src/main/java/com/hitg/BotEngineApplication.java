package com.hitg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.hitg.service.FirstService;

@SpringBootApplication
public class BotEngineApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BotEngineApplication.class, args);
		((FirstService)context.getBean("firstService")).sayHello();
	}
}
