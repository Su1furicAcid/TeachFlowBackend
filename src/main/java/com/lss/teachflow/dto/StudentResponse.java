package com.lss.teachflow.dto;

import com.lss.teachflow.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long id;
    private String studentNumber;
    private Long teacherId;
    private String studentName;
    private String grade;
    private String clazz;

    public static StudentResponse fromEntity(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .studentNumber(s.getStudentNumber())
                .teacherId(s.getTeacherId())
                .studentName(s.getStudentName())
                .grade(s.getGrade())
                .clazz(s.getClazz())
                .build();
    }
}
