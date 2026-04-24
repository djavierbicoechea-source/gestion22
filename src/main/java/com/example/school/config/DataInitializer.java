package com.example.school.config;

import com.example.school.entity.Role;
import com.example.school.entity.User;
import com.example.school.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {

            if (!userRepository.existsByUsername("admin")) {
                userRepository.save(
                    new User("admin",
                    passwordEncoder.encode("school2026"),
                    Role.ADMIN)
                );
            }

            if (!userRepository.existsByUsername("prof1")) {
                userRepository.save(
                    new User("prof1",
                    passwordEncoder.encode("teach2026"),
                    Role.PROFESSOR)
                );
            }

            if (!userRepository.existsByUsername("student1")) {
                userRepository.save(
                    new User("student1",
                    passwordEncoder.encode("learn2026"),
                    Role.STUDENT)
                );
            }
        };
    }
}
