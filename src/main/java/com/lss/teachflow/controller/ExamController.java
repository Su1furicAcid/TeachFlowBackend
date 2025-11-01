package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.ExamUploadRequest;
import com.lss.teachflow.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseBody<Long> uploadExam(@RequestBody ExamUploadRequest examUploadRequest) {
        Long examId = examService.uploadExam(examUploadRequest);
        return ResponseBody.success(examId, "Exam uploaded successfully!");
    }
}
