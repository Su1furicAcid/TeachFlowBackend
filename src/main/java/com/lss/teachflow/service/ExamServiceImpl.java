package com.lss.teachflow.service;

import com.lss.teachflow.dto.ExamUploadRequest;
import com.lss.teachflow.entity.Exam;
import com.lss.teachflow.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor // 使用 Lombok 自动生成构造函数并注入依赖
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    @Override
    @Transactional
    public Long uploadExam(ExamUploadRequest examUploadRequest) {
        Exam exam = new Exam();
        exam.setExamName(examUploadRequest.getExamName());
        exam.setExamDate(LocalDate.parse(examUploadRequest.getExamDate()));
        Exam savedExam = examRepository.save(exam);
        return savedExam.getExamId();
    }
}
