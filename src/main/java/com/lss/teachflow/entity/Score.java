package com.lss.teachflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "scores", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "exam_id"}) // 学生和考试的组合必须唯一
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam examId;

    @Column(precision = 5, scale = 2)
    private BigDecimal scoreValue;
}
