package com.example.school.service;

import com.example.school.entity.Grade;

import java.util.List;

public interface GradeService {
  List<Grade> findAll();
  Grade findById(Long id);
  Grade save(Grade grade);
  void deleteById(Long id);

  List<Grade> findByStudentId(Long studentId);
  List<Grade> findBySubjectId(Long subjectId);
  Grade upsert(Long studentId, Long subjectId, Double value);
}
