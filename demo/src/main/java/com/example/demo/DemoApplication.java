package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		// create and run the IOC container
		// it has getBean method to create object of a class for you
		// if you want IOC container to manage objects for you, add @Bean annotation to classes.
		SpringApplication.run(DemoApplication.class, args);
	}
}
