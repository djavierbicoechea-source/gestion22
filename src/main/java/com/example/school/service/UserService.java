package com.example.school.service;

import com.example.school.entity.User;

public interface UserService {
  User findByUsername(String username);
  User save(User user);
  boolean existsByUsername(String username);

    User getCurrentUser();
}
