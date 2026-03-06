package com.example.school.service;

import com.example.school.entity.Student;

import java.util.List;

public interface StudentService {
  List<Student> findAll();
  Student findById(Long id);
  Student save(Student student);
  void deleteById(Long id);
}
