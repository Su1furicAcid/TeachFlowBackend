package com.lss.teachflow.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ScoreUploadRequest {
    private Long studentId;
    private BigDecimal scoreValue;
}
