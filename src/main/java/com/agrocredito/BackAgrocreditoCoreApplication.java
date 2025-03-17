package com.agrocredito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"com.agrocredito"})
public class BackAgrocreditoCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackAgrocreditoCoreApplication.class, args);
	}

}
