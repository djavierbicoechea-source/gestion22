package com.example.school.controller;

import com.example.school.entity.Role;
import com.example.school.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostLoginController {

  private final UserService userService;

  public PostLoginController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/post-login")
  public String postLogin(Authentication auth) {
    var user = userService.getCurrentUser();
    if (user.getRole() == Role.ADMIN) return "redirect:/admin/dashboard";
    if (user.getRole() == Role.PROFESSOR) return "redirect:/professor/dashboard";
    return "redirect:/student/dashboard";
  }
}
