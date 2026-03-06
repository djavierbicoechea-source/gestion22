package com.example.school.service.impl;

import com.example.school.entity.Grade;
import com.example.school.exception.NotFoundException;
import com.example.school.repository.GradeRepository;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.SubjectRepository;
import com.example.school.service.GradeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

  private final GradeRepository repo;
  private final StudentRepository students;
  private final SubjectRepository subjects;

  public GradeServiceImpl(GradeRepository repo, StudentRepository students, SubjectRepository subjects) {
    this.repo = repo;
    this.students = students;
    this.subjects = subjects;
  }

  @Override
  public List<Grade> findAll() {
    return repo.findAll();
  }

  @Override
  public Grade findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Grade not found: " + id));
  }

  @Override
  public Grade save(Grade grade) {
    return repo.save(grade);
  }

  @Override
  public void deleteById(Long id) {
    repo.deleteById(id);
  }

  @Override
  public List<Grade> findByStudentId(Long studentId) {
    return repo.findByStudentId(studentId);
  }

  @Override
  public List<Grade> findBySubjectId(Long subjectId) {
    return repo.findBySubjectId(subjectId);
  }

  @Override
  public Grade upsert(Long studentId, Long subjectId, Double value) {
    var student = students.findById(studentId)
        .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
    var subject = subjects.findById(subjectId)
        .orElseThrow(() -> new NotFoundException("Subject not found: " + subjectId));

    Grade g = repo.findByStudentIdAndSubjectId(studentId, subjectId).orElseGet(Grade::new);
    g.setStudent(student);
    g.setSubject(subject);
    g.setValue(value);
    return repo.save(g);
  }
}
