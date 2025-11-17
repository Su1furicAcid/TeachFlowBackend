package com.lss.teachflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SearchRequest {
    @NotBlank
    private String query;

    @NotBlank
    private List<SearchFilter> filters;
}
