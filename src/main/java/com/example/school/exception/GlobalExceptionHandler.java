package com.example.school.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public String handleNotFound(NotFoundException ex, Model model) {
    model.addAttribute("message", ex.getMessage());
    return "error/404";
  }

  @ExceptionHandler(Exception.class)
  public String handleGeneric(Exception ex, Model model) {
    model.addAttribute("message", ex.getMessage());
    return "error/500";
  }
}
