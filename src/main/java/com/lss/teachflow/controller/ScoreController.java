package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.ScoreUploadRequest;
import com.lss.teachflow.entity.Score;
import com.lss.teachflow.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping("/score/exam/{examId}")
    public ResponseBody<?> uploadScoresForExam(
            @PathVariable Long examId,
            @RequestBody List<ScoreUploadRequest> scoreRequests) {
        scoreService.batchUpdateScores(examId, scoreRequests);
        return ResponseBody.success(null, "Scores updated successfully.");
    }

    @GetMapping("/score/exam/{examId}")
    public ResponseBody<List<Score>> getScoresByExam(@PathVariable Long examId) {
        List<Score> scores = scoreService.getScoresByExamId(examId);
        return ResponseBody.success(scores);
    }

    @GetMapping("/score/student/{studentId}")
    public ResponseBody<List<Score>> getScoresByStudent(@PathVariable Long studentId) {
        List<Score> scores = scoreService.getScoresByStudentId(studentId);
        return ResponseBody.success(scores);
    }
}
