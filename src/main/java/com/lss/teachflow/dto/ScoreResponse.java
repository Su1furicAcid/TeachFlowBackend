package com.lss.teachflow.dto;

import com.lss.teachflow.entity.Score;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreResponse {
    Long scoreId;
    String studentNumber;
    String studentName;
    String examName;
    String examSubject;
    BigDecimal scoreValue;
    LocalDate examDate;

    public static ScoreResponse fromEntity(Score score) {
        return ScoreResponse.builder()
                .scoreId(score.getId())
                .studentNumber(score.getStudent().getStudentNumber())
                .studentName(score.getStudent().getStudentName())
                .examName(score.getExam().getExamName())
                .examSubject(score.getExam().getExamSubject())
                .scoreValue(score.getScoreValue())
                .examDate(score.getExam().getExamDate())
                .build();
    }
}
