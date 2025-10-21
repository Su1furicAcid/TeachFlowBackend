package com.lss.teachflow.controller;

import com.lss.teachflow.dto.ScoreUploadRequest;
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

    @PostMapping("/exam/{examId}")
    public ResponseEntity<?> uploadScoresForExam(
            @PathVariable Long examId,
            @RequestBody List<ScoreUploadRequest> scoreRequests) {
        scoreService.batchUpdateScores(examId, scoreRequests);
        return ResponseEntity.ok("Scores updated successfully.");
    }
}
