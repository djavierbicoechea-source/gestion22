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
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("school2026"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("prof1")) {
                User prof = new User();
                prof.setUsername("prof1");
                prof.setPassword(passwordEncoder.encode("teach2026"));
                prof.setRole(Role.PROFESSOR);
                userRepository.save(prof);
            }

            if (!userRepository.existsByUsername("student1")) {
                User student = new User();
                student.setUsername("student1");
                student.setPassword(passwordEncoder.encode("learn2026"));
                student.setRole(Role.STUDENT);
                userRepository.save(student);
            }
        };
    }
}
