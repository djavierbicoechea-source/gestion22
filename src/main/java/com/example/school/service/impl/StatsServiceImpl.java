package com.example.school.service.impl;

import com.example.school.repository.GradeRepository;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.SubjectRepository;
import com.example.school.service.StatsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

  private final GradeRepository gradeRepository;
  private final SubjectRepository subjectRepository;
  private final StudentRepository studentRepository;

  public StatsServiceImpl(GradeRepository gradeRepository, SubjectRepository subjectRepository, StudentRepository studentRepository) {
    this.gradeRepository = gradeRepository;
    this.subjectRepository = subjectRepository;
    this.studentRepository = studentRepository;
  }

  @Override
  public double globalWeightedAverage() {
    var grades = gradeRepository.findAll();
    double sum = 0.0;
    double coefSum = 0.0;
    for (var g : grades) {
      int coef = (g.getSubject() != null && g.getSubject().getCoefficient() != null) ? g.getSubject().getCoefficient() : 1;
      sum += g.getValue() * coef;
      coefSum += coef;
    }
    return coefSum == 0.0 ? 0.0 : sum / coefSum;
  }

  @Override
  public List<SubjectAverage> averagesBySubject() {
    var subjects = subjectRepository.findAll();
    var grades = gradeRepository.findAll();

    List<SubjectAverage> out = new ArrayList<>();
    for (var s : subjects) {
      double sum = 0.0;
      long count = 0;
      for (var g : grades) {
        if (g.getSubject() != null && g.getSubject().getId().equals(s.getId())) {
          sum += g.getValue();
          count++;
        }
      }
      double avg = count == 0 ? 0.0 : sum / count;
      out.add(new SubjectAverage(s.getId(), s.getName(), avg, count));
    }
    out.sort(Comparator.comparing(SubjectAverage::subjectName));
    return out;
  }

  @Override
  public List<StudentAverage> averagesByStudent() {
    var students = studentRepository.findAll();
    var grades = gradeRepository.findAll();

    List<StudentAverage> out = new ArrayList<>();
    for (var st : students) {
      double sum = 0.0;
      double coefSum = 0.0;
      for (var g : grades) {
        if (g.getStudent() != null && g.getStudent().getId().equals(st.getId())) {
          int coef = (g.getSubject() != null && g.getSubject().getCoefficient() != null) ? g.getSubject().getCoefficient() : 1;
          sum += g.getValue() * coef;
          coefSum += coef;
        }
      }
      double avg = coefSum == 0.0 ? 0.0 : sum / coefSum;
      out.add(new StudentAverage(st.getId(), st.getFirstName() + " " + st.getLastName(), st.getClassroom(), avg));
    }
    out.sort(Comparator.comparing(StudentAverage::weightedAverage).reversed());
    return out;
  }
}
