package com.lss.teachflow.repository;

import com.lss.teachflow.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByExamId_ExamId(Long examIdExamId);
    List<Score> findByStudentId_StudentId(Long studentId);
}
