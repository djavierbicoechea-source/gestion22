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
    CommandLineRunner init(UserRepository repo,
                           PasswordEncoder encoder) {
        return args -> {

            repo.findByUsername("admin").ifPresent(repo::delete);
            repo.findByUsername("student1").ifPresent(repo::delete);
            repo.findByUsername("prof1").ifPresent(repo::delete);

            repo.save(new User(
                    "admin",
                    encoder.encode("school2026"),
                    Role.ADMIN));

            repo.save(new User(
                    "student1",
                    encoder.encode("learn2026"),
                    Role.STUDENT));

            repo.save(new User(
                    "prof1",
                    encoder.encode("teach2026"),
                    Role.PROFESSOR));

            System.out.println("Users recreated OK");
        };
    }
}
