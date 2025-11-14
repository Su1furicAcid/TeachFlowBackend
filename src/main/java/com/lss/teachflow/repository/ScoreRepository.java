package com.lss.teachflow.repository;

import com.lss.teachflow.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByExam_ExamId(Long examId);
    List<Score> findByStudent_Id(Long studentId);
    java.util.Optional<Score> findByExam_ExamIdAndStudent_Id(Long examId, Long studentId);
}
