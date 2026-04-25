package com.example.school.repository;

import com.example.school.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
  List<Grade> findByStudentId(Long studentId);
  List<Grade> findBySubjectId(Long subjectId);
  Optional<Grade> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
  void deleteByStudentId(Long studentId);
  void deleteBySubjectId(Long subjectId);
}
