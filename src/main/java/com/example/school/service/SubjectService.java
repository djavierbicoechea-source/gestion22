package com.example.school.service;

import com.example.school.entity.Subject;

import java.util.List;

public interface SubjectService {
  List<Subject> findAll();
  Subject findById(Long id);
  Subject save(Subject subject);
  void deleteById(Long id);
}
