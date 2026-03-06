package com.example.school.controller;

import com.example.school.service.ProfessorService;
import com.example.school.service.StatsService;
import com.example.school.service.StudentService;
import com.example.school.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final StudentService studentService;
  private final ProfessorService professorService;
  private final SubjectService subjectService;
  private final StatsService statsService;

  public AdminController(StudentService studentService, ProfessorService professorService, SubjectService subjectService, StatsService statsService) {
    this.studentService = studentService;
    this.professorService = professorService;
    this.subjectService = subjectService;
    this.statsService = statsService;
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model) {
    model.addAttribute("studentsCount", studentService.findAll().size());
    model.addAttribute("professorsCount", professorService.findAll().size());
    model.addAttribute("subjectsCount", subjectService.findAll().size());
    model.addAttribute("globalAverage", statsService.globalWeightedAverage());
    return "admin/dashboard";
  }
}
