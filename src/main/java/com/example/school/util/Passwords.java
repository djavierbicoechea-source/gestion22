package com.example.school.util;

import java.security.SecureRandom;

public final class Passwords {
  private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
  private static final SecureRandom RNG = new SecureRandom();

  private Passwords() {}

  public static String generate() {
    return generate(10);
  }

  public static String generate(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(CHARS.charAt(RNG.nextInt(CHARS.length())));
    }
    return sb.toString();
  }
}
