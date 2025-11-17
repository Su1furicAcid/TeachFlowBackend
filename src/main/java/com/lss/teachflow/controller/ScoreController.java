package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.ScoreResponse;
import com.lss.teachflow.dto.ScoreUploadRequest;
import com.lss.teachflow.entity.Score;
import com.lss.teachflow.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scores")
@RequiredArgsConstructor
@Tag(name = "成绩管理", description = "上传和查询学生成绩")
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping("/score/exam/{examId}")
    @Operation(summary = "批量上传成绩", description = "为某次考试批量上传学生成绩")
    public ResponseBody<?> uploadScoresForExam(
            @PathVariable Long examId,
            @RequestBody List<ScoreUploadRequest> scoreRequests) {
        scoreService.batchUpdateScores(examId, scoreRequests);
        return ResponseBody.success(null, "Scores updated successfully.");
    }

    @GetMapping("/score/exam/{examId}")
    @Operation(summary = "查询考试成绩", description = "根据考试ID查询所有学生成绩")
    public ResponseBody<List<ScoreResponse>> getScoresByExam(@PathVariable Long examId) {
        List<ScoreResponse> scores = scoreService.getScoresByExamId(examId);
        return ResponseBody.success(scores);
    }

    @GetMapping("/score/student/{studentId}")
    @Operation(summary = "查询学生成绩", description = "根据学生ID查询该学生所有考试成绩")
    public ResponseBody<List<ScoreResponse>> getScoresByStudent(@PathVariable Long studentId) {
        List<ScoreResponse> scores = scoreService.getScoresByStudentId(studentId);
        return ResponseBody.success(scores);
    }
}
