package com.lss.teachflow.service;

import com.lss.teachflow.dto.ExamResponse;
import com.lss.teachflow.dto.ExamUploadRequest;
import com.lss.teachflow.entity.Exam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExamService {
    Long uploadExam(ExamUploadRequest examUploadRequest, MultipartFile file);
    List<ExamResponse> getAllExams();
}
