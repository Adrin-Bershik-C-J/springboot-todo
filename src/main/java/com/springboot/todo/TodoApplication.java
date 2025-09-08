package com.springboot.todo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springboot.todo.entity.User;
import com.springboot.todo.enums.Role;
import com.springboot.todo.repository.UserRepository;

@SpringBootApplication
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User(
						"System Admin",
						"admin",
						passwordEncoder.encode("adminpass"),
						Role.ADMIN);
				userRepository.save(admin);
			}
		};
	}

}
