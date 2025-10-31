package com.lss.teachflow.service;

import com.lss.teachflow.dto.ScoreUploadRequest;
import com.lss.teachflow.entity.Score;

import java.util.List;

public interface ScoreService {
    void batchUpdateScores(Long examId, List<ScoreUploadRequest> scoreRequests);
    List<Score> getScoresByExamId(Long examId);
    List<Score> getScoresByStudentId(Long studentId);
}
