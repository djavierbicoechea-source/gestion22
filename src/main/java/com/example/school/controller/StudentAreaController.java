package com.example.school.controller;

import com.example.school.entity.Role;
import com.example.school.service.BulletinPdfService;
import com.example.school.service.GradeService;
import com.example.school.service.UserService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentAreaController {

  private final UserService userService;
  private final GradeService gradeService;
  private final BulletinPdfService bulletinPdfService;

  public StudentAreaController(UserService userService, GradeService gradeService, BulletinPdfService bulletinPdfService) {
    this.userService = userService;
    this.gradeService = gradeService;
    this.bulletinPdfService = bulletinPdfService;
  }

  @GetMapping("/dashboard")
  public String dashboard(Authentication authentication, Model model) {
    var user = userService.findByUsername(authentication.getName());
    if (user.getRole() != Role.STUDENT || user.getStudent() == null) {
      return "redirect:/post-login";
    }
    var student = user.getStudent();
    model.addAttribute("student", student);
    model.addAttribute("bulletinUrl", "/student/bulletin");
    model.addAttribute("pdfUrl", "/student/bulletin/pdf");
    return "student/dashboard";
  }

  @GetMapping("/bulletin")
  public String bulletin(Authentication authentication, Model model) {
    var user = userService.findByUsername(authentication.getName());
    var student = user.getStudent();
    var grades = gradeService.findByStudentId(student.getId());

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
    model.addAttribute("pdfUrl", "/student/bulletin/pdf");
    model.addAttribute("backUrl", "/student/dashboard");
    return "student/bulletin";
  }

  @GetMapping("/bulletin/pdf")
  public ResponseEntity<byte[]> bulletinPdf(Authentication authentication) {
    var user = userService.findByUsername(authentication.getName());
    var student = user.getStudent();
    byte[] pdf = bulletinPdfService.generateStudentBulletinPdf(student.getId());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename("bulletin-" + student.getId() + ".pdf").build());

    return ResponseEntity.ok().headers(headers).body(pdf);
  }
}
