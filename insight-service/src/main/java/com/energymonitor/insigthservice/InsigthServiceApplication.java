package com.energymonitor.insigthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class InsigthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsigthServiceApplication.class, args);
	}

}
