package com.lss.teachflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long teacherId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private String clazz;

    @Column(name = "student_number", unique = true, nullable = false)
    private String studentNumber;
}
