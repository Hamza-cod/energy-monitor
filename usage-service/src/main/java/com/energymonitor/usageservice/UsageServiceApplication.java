package com.energymonitor.usageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
@EnableResilientMethods
public class UsageServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(UsageServiceApplication.class, args);
	}

}
