package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.ExamUploadRequest;
import com.lss.teachflow.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
@Tag(name = "考试管理", description = "上传和管理考试信息")
public class ExamController {

    private final ExamService examService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "上传考试信息", description = "上传一次考试的详细信息，包括试卷文件")
    public ResponseBody<Long> uploadExam(@RequestParam("examName") String examName,
                                         @RequestParam("examSubject") String examSubject,
                                         @RequestParam("examDate") String examDate,
                                         @RequestPart("file") MultipartFile file) {
        ExamUploadRequest examUploadRequest = new ExamUploadRequest();
        examUploadRequest.setExamName(examName);
        examUploadRequest.setExamSubject(examSubject);
        examUploadRequest.setExamDate(examDate);
        Long examId = examService.uploadExam(examUploadRequest, file);
        return ResponseBody.success(examId, "Exam uploaded successfully!");
    }

    @GetMapping
    @Operation(summary = "获取考试信息", description = "获取所有考试的详细信息")
    public ResponseBody<?> getAllExams() {
        return ResponseBody.success(examService.getAllExams(), "Exams retrieved successfully!");
    }
}
