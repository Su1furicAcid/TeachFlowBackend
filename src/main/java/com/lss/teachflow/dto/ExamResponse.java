package com.lss.teachflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private Long examId;
    private String examName;
    private String examSubject;
    private String examDate;
    private String filePath;
}
