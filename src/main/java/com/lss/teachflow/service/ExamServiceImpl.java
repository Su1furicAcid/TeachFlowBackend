package com.lss.teachflow.service;

import com.lss.teachflow.dto.ExamResponse;
import com.lss.teachflow.dto.ExamUploadRequest;
import com.lss.teachflow.entity.Exam;
import com.lss.teachflow.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public Long uploadExam(ExamUploadRequest examUploadRequest, MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        Exam exam = new Exam();
        exam.setExamName(examUploadRequest.getExamName());
        exam.setExamDate(LocalDate.parse(examUploadRequest.getExamDate()));
        exam.setExamSubject(examUploadRequest.getExamSubject());
        exam.setFilePath(fileName);
        Exam savedExam = examRepository.save(exam);
        return savedExam.getExamId();
    }

    @Override
    public List<ExamResponse> getAllExams() {
        return examRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ExamResponse convertToResponse(Exam exam) {
        return ExamResponse.builder()
                .examId(exam.getExamId())
                .examName(exam.getExamName())
                .examSubject(exam.getExamSubject())
                .examDate(exam.getExamDate().toString())
                .filePath(exam.getFilePath())
                .build();
    }
}
