package com.lss.teachflow.repository;

import com.lss.teachflow.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findByExamId(Long examId);
    Optional<Exam> findByExamName(String examName);
}
