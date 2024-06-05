package com.example.oopclass;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@RequiredArgsConstructor
public class OopClassApplication implements CommandLineRunner {

	private final RedisTemplate<String, String> stringValueRedisTemplate;


	public static void main(String[] args) {
		SecurityContextHolder.clearContext(); // 애플리케이션 시작 시 인증 정보 초기화
		SpringApplication.run(OopClassApplication.class, args);
	}

	@Override
	public void run(final String... args) {

		// TEST: String 메시지 전송
		stringValueRedisTemplate.convertAndSend("ch01", "Apple, Orange");
	}
}





