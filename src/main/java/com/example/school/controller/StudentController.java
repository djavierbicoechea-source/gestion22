package com.example.school.controller;

import com.example.school.entity.Student;
import com.example.school.service.BulletinPdfService;
import com.example.school.service.GradeService;
import com.example.school.service.StudentService;
import com.example.school.entity.Role;
import com.example.school.entity.User;
import com.example.school.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.school.util.Passwords;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/students")
public class StudentController {

  private final StudentService studentService;
  private final GradeService gradeService;
  private final BulletinPdfService bulletinPdfService;

  public StudentController(StudentService studentService, GradeService gradeService, BulletinPdfService bulletinPdfService) {
    this.studentService = studentService;
    this.gradeService = gradeService;
    this.bulletinPdfService = bulletinPdfService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("students", studentService.findAll());
    return "admin/students/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("student", new Student());
    return "admin/students/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("student") Student student, BindingResult result) {
    if (result.hasErrors()) return "admin/students/form";
    studentService.save(student);
    return "redirect:/admin/students";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("student", studentService.findById(id));
    return "admin/students/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("student") Student student, BindingResult result) {
    if (result.hasErrors()) return "admin/students/form";
    student.setId(id);
    studentService.save(student);
    return "redirect:/admin/students";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    studentService.deleteById(id);
    return "redirect:/admin/students";
  }

  @GetMapping("/{id}/bulletin")
  public String bulletin(@PathVariable Long id, Model model) {
    var student = studentService.findById(id);
    var grades = gradeService.findByStudentId(id);

    double sum = 0.0;
    double coefSum = 0.0;
    for (var g : grades) {
      int coef = (g.getSubject() != null && g.getSubject().getCoefficient() != null) ? g.getSubject().getCoefficient() : 1;
      sum += g.getValue() * coef;
      coefSum += coef;
    }
    double weightedAverage = (coefSum == 0.0) ? 0.0 : (sum / coefSum);

    model.addAttribute("student", student);
    model.addAttribute("grades", grades);
    model.addAttribute("weightedAverage", weightedAverage);
    model.addAttribute("pdfUrl", "/admin/students/" + id + "/bulletin/pdf");
    model.addAttribute("backUrl", "/admin/students");
    return "student/bulletin";
  }

  @GetMapping("/{id}/bulletin/pdf")
  public ResponseEntity<byte[]> bulletinPdf(@PathVariable Long id) {
    byte[] pdf = bulletinPdfService.generateStudentBulletinPdf(id);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename("bulletin-student-" + id + ".pdf").build());

    return ResponseEntity.ok().headers(headers).body(pdf);
  }
}
