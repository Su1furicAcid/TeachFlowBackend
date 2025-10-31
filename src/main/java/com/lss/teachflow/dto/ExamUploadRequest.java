package com.lss.teachflow.dto;

import lombok.Data;

@Data
public class ExamUploadRequest {
    private String examName;
    private String examSubject;
    private String examDate;
}
