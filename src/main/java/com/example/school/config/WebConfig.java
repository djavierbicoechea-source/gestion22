package com.example.school.config;

import com.example.school.entity.Professor;
import com.example.school.entity.Student;
import com.example.school.entity.Subject;
import com.example.school.service.ProfessorService;
import com.example.school.service.StudentService;
import com.example.school.service.SubjectService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final ProfessorService professorService;
  private final StudentService studentService;
  private final SubjectService subjectService;

  public WebConfig(ProfessorService professorService, StudentService studentService, SubjectService subjectService) {
    this.professorService = professorService;
    this.studentService = studentService;
    this.subjectService = subjectService;
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(String.class, Professor.class, source -> {
      if (source == null || source.isBlank()) return null;
      return professorService.findById(Long.valueOf(source));
    });

    registry.addConverter(String.class, Student.class, source -> {
      if (source == null || source.isBlank()) return null;
      return studentService.findById(Long.valueOf(source));
    });

    registry.addConverter(String.class, Subject.class, source -> {
      if (source == null || source.isBlank()) return null;
      return subjectService.findById(Long.valueOf(source));
    });
  }
}
