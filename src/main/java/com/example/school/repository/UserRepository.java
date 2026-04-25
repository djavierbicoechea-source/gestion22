package com.example.school.repository;

import com.example.school.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  boolean existsByUsername(String username);
  Optional<User> findByStudentId(Long studentId);
  Optional<User> findByProfessorId(Long professorId);
  void deleteByStudentId(Long studentId);
  void deleteByProfessorId(Long professorId);
}
