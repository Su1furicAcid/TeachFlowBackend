package com.lss.teachflow.repository;

import com.lss.teachflow.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByExamId(Long examId);
}
