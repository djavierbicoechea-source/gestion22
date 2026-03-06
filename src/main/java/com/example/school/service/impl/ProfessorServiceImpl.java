package com.example.school.service.impl;

import com.example.school.entity.Professor;
import com.example.school.exception.NotFoundException;
import com.example.school.repository.ProfessorRepository;
import com.example.school.service.ProfessorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

  private final ProfessorRepository repo;

  public ProfessorServiceImpl(ProfessorRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Professor> findAll() {
    return repo.findAll();
  }

  @Override
  public Professor findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Professor not found: " + id));
  }

  @Override
  public Professor save(Professor professor) {
    return repo.save(professor);
  }

  @Override
  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
