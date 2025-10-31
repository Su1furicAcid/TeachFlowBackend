package com.lss.teachflow.controller;

import com.lss.teachflow.dto.ScoreUploadRequest;
import com.lss.teachflow.entity.Score;
import com.lss.teachflow.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @PostMapping("/score/exam/{examId}")
    public ResponseEntity<?> uploadScoresForExam(
            @PathVariable Long examId,
            @RequestBody List<ScoreUploadRequest> scoreRequests) {
        scoreService.batchUpdateScores(examId, scoreRequests);
        return ResponseEntity.ok("Scores updated successfully.");
    }

    @GetMapping("/score/exam/{examId}")
    public ResponseEntity<List<Score>> getScoresByExam(@PathVariable Long examId) {
        List<Score> scores = scoreService.getScoresByExamId(examId);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/score/student/{studentId}")
    public ResponseEntity<List<Score>> getScoresByStudent(@PathVariable Long studentId) {
        List<Score> scores = scoreService.getScoresByStudentId(studentId);
        return ResponseEntity.ok(scores);
    }
}
