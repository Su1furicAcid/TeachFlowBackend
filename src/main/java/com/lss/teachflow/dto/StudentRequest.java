package com.lss.teachflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {
    @NotBlank
    private String studentNumber;

    @NotBlank
    private String studentName;

    @NotBlank
    private String grade;

    @NotBlank
    private String clazz;
}
