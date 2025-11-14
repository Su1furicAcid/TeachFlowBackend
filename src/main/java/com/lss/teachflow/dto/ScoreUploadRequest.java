package com.lss.teachflow.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ScoreUploadRequest {
    private String studentNumber;
    private BigDecimal scoreValue;
}
