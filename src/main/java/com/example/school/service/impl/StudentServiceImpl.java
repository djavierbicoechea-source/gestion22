package com.example.school.service.impl;

import com.example.school.entity.Student;
import com.example.school.exception.NotFoundException;
import com.example.school.repository.StudentRepository;
import com.example.school.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

  private final StudentRepository repo;

  public StudentServiceImpl(StudentRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Student> findAll() {
    return repo.findAll();
  }

  @Override
  public Student findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Student not found: " + id));
  }

  @Override
  public Student save(Student student) {
    return repo.save(student);
  }

  @Override
  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
