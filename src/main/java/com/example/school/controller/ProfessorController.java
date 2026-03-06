package com.example.school.controller;

import com.example.school.entity.Professor;
import com.example.school.service.ProfessorService;
import com.example.school.entity.Role;
import com.example.school.entity.User;
import com.example.school.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.school.util.Passwords;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/professors")
public class ProfessorController {

  private final ProfessorService service;
  private final UserRepository users;
  private final PasswordEncoder encoder;

  public ProfessorController(ProfessorService service, UserRepository users, PasswordEncoder encoder) {
    this.service = service;
    this.users = users;
    this.encoder = encoder;
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
  public String create(@Valid @ModelAttribute("professor") Professor professor, BindingResult result) {
    if (result.hasErrors()) return "admin/professors/form";
    service.save(professor);
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

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    service.deleteById(id);
    return "redirect:/admin/professors";
  }
}
