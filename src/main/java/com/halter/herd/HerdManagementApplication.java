package com.halter.herd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HerdManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerdManagementApplication.class, args);
	}

}
