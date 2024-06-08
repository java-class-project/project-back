package com.example.oopclass;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@SpringBootApplication(exclude = SpringApplicationAdminJmxAutoConfiguration.class)
public class OopClassApplication implements CommandLineRunner {

	private final RedisTemplate<String, String> stringValueRedisTemplate;

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(OopClassApplication.class, args);

		SecurityContextHolder.clearContext(); // 애플리케이션 시작 시 인증 정보 초기화
	}

	@Override
	public void run(final String... args) {
		// TEST: String 메시지 전송
		stringValueRedisTemplate.convertAndSend("ch01", "Apple, Orange");
	}
}
