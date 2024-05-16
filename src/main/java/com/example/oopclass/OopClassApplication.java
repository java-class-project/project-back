package com.example.oopclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.oopclass.domain"})
@EnableJpaRepositories(basePackages = {"com.example.oopclass.domain"})
public class OopClassApplication {

	public static void main(String[] args) {
		SpringApplication.run(OopClassApplication.class, args);
	}
}
