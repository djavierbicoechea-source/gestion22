package com.example.school.service;

public interface BulletinPdfService {
  byte[] generateStudentBulletinPdf(Long studentId);
}
