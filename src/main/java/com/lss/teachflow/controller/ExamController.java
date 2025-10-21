package com.lss.teachflow.controller;

import com.lss.teachflow.dto.ExamUploadRequest;
import com.lss.teachflow.entity.Exam;
import com.lss.teachflow.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<Long> createExam(@RequestBody ExamUploadRequest examUploadRequest) {
        Long examId = examService.uploadExam(examUploadRequest);
        return new ResponseEntity<>(examId, HttpStatus.CREATED);
    }
}
