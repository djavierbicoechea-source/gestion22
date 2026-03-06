package com.example.school.controller;

import com.example.school.entity.Role;
import com.example.school.exception.NotFoundException;
import com.example.school.repository.SubjectRepository;
import com.example.school.service.GradeService;
import com.example.school.service.StudentService;
import com.example.school.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/professor")
public class ProfessorAreaController {

  private final UserService userService;
  private final SubjectRepository subjects;
  private final StudentService students;
  private final GradeService grades;

  public ProfessorAreaController(UserService userService, SubjectRepository subjects, StudentService students, GradeService grades) {
    this.userService = userService;
    this.subjects = subjects;
    this.students = students;
    this.grades = grades;
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model) {
    var user = userService.getCurrentUser();
    if (user.getRole() != Role.PROFESSOR) return "redirect:/post-login";

    var prof = user.getProfessor();
    if (prof == null) throw new NotFoundException("Compte professeur non lié à une fiche professeur.");
    model.addAttribute("professor", prof);
    model.addAttribute("subjects", subjects.findByProfessor_Id(prof.getId()));
    return "professor/dashboard";
  }

  @GetMapping("/grades")
  public String chooseSubject(@RequestParam(name = "subjectId", required = false) Long subjectId, Model model) {
    var user = userService.getCurrentUser();
    if (user.getRole() != Role.PROFESSOR) return "redirect:/post-login";
    var prof = user.getProfessor();
    if (prof == null) throw new NotFoundException("Compte professeur non lié à une fiche professeur.");

    var mySubjects = subjects.findByProfessor_Id(prof.getId());
    model.addAttribute("subjects", mySubjects);
    model.addAttribute("subjectId", subjectId);

    if (subjectId != null) {
      var subject = subjects.findById(subjectId)
          .orElseThrow(() -> new NotFoundException("Subject not found: " + subjectId));
      if (subject.getProfessor() == null || !subject.getProfessor().getId().equals(prof.getId())) {
        // interdit: un professeur ne peut pas noter une matière qui n'est pas à lui
        return "redirect:/professor/grades?forbidden";
      }
      model.addAttribute("subject", subject);
      model.addAttribute("students", students.findAll());
    }

    return "professor/grades";
  }

  public static class GradeForm {
    @NotNull
    private Long subjectId;

    @NotNull
    private Long studentId;

    @NotNull
    @Min(0) @Max(20)
    private Double value;

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
  }

  @PostMapping("/grades")
  public String saveGrade(@ModelAttribute("form") @NotNull GradeForm form,
                          BindingResult result,
                          RedirectAttributes ra) {
    var user = userService.getCurrentUser();
    if (user.getRole() != Role.PROFESSOR) return "redirect:/post-login";
    var prof = user.getProfessor();
    if (prof == null) throw new NotFoundException("Compte professeur non lié à une fiche professeur.");

    var subject = subjects.findById(form.getSubjectId())
        .orElseThrow(() -> new NotFoundException("Subject not found: " + form.getSubjectId()));
    if (subject.getProfessor() == null || !subject.getProfessor().getId().equals(prof.getId())) {
      return "redirect:/professor/grades?forbidden";
    }

    grades.upsert(form.getStudentId(), form.getSubjectId(), form.getValue());
    ra.addFlashAttribute("savedMsg", "Note enregistrée.");
    return "redirect:/professor/grades?subjectId=" + form.getSubjectId();
  }
}
