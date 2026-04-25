package com.example.school.controller;

import com.example.school.entity.Professor;
import com.example.school.entity.Role;
import com.example.school.entity.User;
import com.example.school.repository.SubjectRepository;
import com.example.school.repository.UserRepository;
import com.example.school.service.ProfessorService;
import com.example.school.util.Passwords;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/professors")
public class ProfessorController {

  private final ProfessorService service;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final SubjectRepository subjectRepository;

  public ProfessorController(ProfessorService service,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             SubjectRepository subjectRepository) {
    this.service = service;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.subjectRepository = subjectRepository;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("professors", service.findAll());
    return "admin/professors/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("professor", new Professor());
    return "admin/professors/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("professor") Professor professor,
                       BindingResult result,
                       RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) return "admin/professors/form";

    Professor savedProfessor = service.save(professor);
    String username = "prof" + savedProfessor.getId();
    String rawPassword = Passwords.generate(10);

    if (!userRepository.existsByUsername(username)) {
      User user = new User(username, passwordEncoder.encode(rawPassword), Role.PROFESSOR);
      user.setProfessor(savedProfessor);
      userRepository.save(user);
      redirectAttributes.addFlashAttribute("createdAccountMsg",
          "Compte professeur créé : username = " + username + " / mot de passe = " + rawPassword);
    }

    return "redirect:/admin/professors";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("professor", service.findById(id));
    return "admin/professors/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("professor") Professor professor, BindingResult result) {
    if (result.hasErrors()) return "admin/professors/form";
    professor.setId(id);
    service.save(professor);
    return "redirect:/admin/professors";
  }

  @Transactional
  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    subjectRepository.findByProfessor_Id(id).forEach(subject -> {
      subject.setProfessor(null);
      subjectRepository.save(subject);
    });
    userRepository.deleteByProfessorId(id);
    service.deleteById(id);
    return "redirect:/admin/professors";
  }
}
