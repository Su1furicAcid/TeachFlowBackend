package com.lss.teachflow.service;

import com.lss.teachflow.dto.ExamUploadRequest;

public interface ExamService {
    Long uploadExam(ExamUploadRequest examUploadRequest);
}
