package com.example.oopclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
public class OopClassApplication {

	public static void main(String[] args) {
		SecurityContextHolder.clearContext(); // 애플리케이션 시작 시 인증 정보 초기화
		SpringApplication.run(OopClassApplication.class, args);
	}
}





