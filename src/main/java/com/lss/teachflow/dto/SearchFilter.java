package com.lss.teachflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

enum SourceType {
    DOCUMENT,
    VIDEO,
    IMAGE
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SearchFilter {
    @NotBlank
    private SourceType sourceType;

    @NotBlank
    private int maxResults;

    @NotBlank
    private boolean safeSearch;
}