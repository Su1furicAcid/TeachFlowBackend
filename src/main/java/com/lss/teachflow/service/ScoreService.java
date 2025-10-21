package com.lss.teachflow.service;

import com.lss.teachflow.dto.ScoreUploadRequest;
import java.util.List;

public interface ScoreService {
    void batchUpdateScores(Long examId, List<ScoreUploadRequest> scoreRequests);
}
