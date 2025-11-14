package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.ExamUploadRequest;
import com.lss.teachflow.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@Tag(name = "考试管理", description = "上传和管理考试信息")
public class ExamController {

    private final ExamService examService;

    @PostMapping
    @Operation(summary = "上传考试信息", description = "上传一次考试的详细信息")
    public ResponseBody<Long> uploadExam(@RequestBody ExamUploadRequest examUploadRequest) {
        Long examId = examService.uploadExam(examUploadRequest);
        return ResponseBody.success(examId, "Exam uploaded successfully!");
    }

    @GetMapping
    @Operation(summary = "获取考试信息", description = "获取所有考试的详细信息")
    public ResponseBody<?> getAllExams() {
        return ResponseBody.success(examService.getAllExams(), "Exams retrieved successfully!");
    }
}
