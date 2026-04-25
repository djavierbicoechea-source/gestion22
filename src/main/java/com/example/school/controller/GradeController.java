package com.example.school.controller;

import com.example.school.entity.Grade;
import com.example.school.service.GradeService;
import com.example.school.service.StudentService;
import com.example.school.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/grades")
public class GradeController {

  private final GradeService gradeService;
  private final StudentService studentService;
  private final SubjectService subjectService;

  public GradeController(GradeService gradeService, StudentService studentService, SubjectService subjectService) {
    this.gradeService = gradeService;
    this.studentService = studentService;
    this.subjectService = subjectService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("grades", gradeService.findAll());
    return "admin/grades/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("grade", new Grade());
    model.addAttribute("students", studentService.findAll());
    model.addAttribute("subjects", subjectService.findAll());
    return "admin/grades/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("grade") Grade grade, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("students", studentService.findAll());
      model.addAttribute("subjects", subjectService.findAll());
      return "admin/grades/form";
    }
    gradeService.save(grade);
    return "redirect:/admin/grades";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("grade", gradeService.findById(id));
    model.addAttribute("students", studentService.findAll());
    model.addAttribute("subjects", subjectService.findAll());
    return "admin/grades/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("grade") Grade grade, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("students", studentService.findAll());
      model.addAttribute("subjects", subjectService.findAll());
      return "admin/grades/form";
    }
    grade.setId(id);
    gradeService.save(grade);
    return "redirect:/admin/grades";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    gradeService.deleteById(id);
    return "redirect:/admin/grades";
  }
}
