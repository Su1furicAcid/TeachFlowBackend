package com.lss.teachflow.service;

import com.lss.teachflow.dto.ExamUploadRequest;
import com.lss.teachflow.entity.Exam;

import java.util.List;

public interface ExamService {
    Long uploadExam(ExamUploadRequest examUploadRequest);
    List<Exam> getAllExams();
}
