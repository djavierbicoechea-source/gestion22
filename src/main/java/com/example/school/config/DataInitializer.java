package com.example.school.config;

import com.example.school.entity.*;
import com.example.school.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner initData(
      UserRepository userRepository,
      StudentRepository studentRepository,
      ProfessorRepository professorRepository,
      SubjectRepository subjectRepository,
      GradeRepository gradeRepository,
      PasswordEncoder passwordEncoder
  ) {
    return args -> {

      // Admin account
      if (!userRepository.existsByUsername("admin")) {
        User admin = new User("admin", passwordEncoder.encode("school2026"), Role.ADMIN);
        userRepository.save(admin);
      }

      // Demo professor & subject
      Professor prof = professorRepository.findAll().stream()
          .filter(p -> "jean.dupont@school.com".equalsIgnoreCase(p.getEmail()))
          .findFirst()
          .orElseGet(() -> professorRepository.findAll().stream().findFirst().orElse(null));

      if (prof == null) {
        prof = professorRepository.save(new Professor("Jean", "Dupont", "jean.dupont@school.com"));
      }

      Subject math = subjectRepository.findAll().stream()
          .filter(s -> "Math".equalsIgnoreCase(s.getName()))
          .findFirst().orElse(null);
      if (math == null) {
        math = new Subject("Math", 2);
        math.setProfessor(prof);
        math = subjectRepository.save(math);
      }

      Subject francais = subjectRepository.findAll().stream()
          .filter(s -> "Francais".equalsIgnoreCase(s.getName()))
          .findFirst().orElse(null);
      if (francais == null) {
        francais = new Subject("Francais", 1);
        francais.setProfessor(prof);
        francais = subjectRepository.save(francais);
      }

      // Demo professor user
      if (!userRepository.existsByUsername("prof1")) {
        User pu = new User("prof1", passwordEncoder.encode("teach2026"), Role.PROFESSOR);
        pu.setProfessor(prof);
        userRepository.save(pu);
      }

      // Demo student + student user
      if (!userRepository.existsByUsername("student1")) {
        Student st = studentRepository.findAll().stream()
            .filter(s -> "marie.paul@student.com".equalsIgnoreCase(s.getEmail()))
            .findFirst()
            .orElseGet(() -> studentRepository.save(new Student("Marie", "Paul", "marie.paul@student.com", "L1")));

        User u = new User("student1", passwordEncoder.encode("learn2026"), Role.STUDENT);
        u.setStudent(st);
        userRepository.save(u);

        // demo grades if none
        if (gradeRepository.findByStudentId(st.getId()).isEmpty()) {
          gradeRepository.save(new Grade(15.5, st, math));
          gradeRepository.save(new Grade(12.0, st, francais));
        }
      }
    };
  }
}
