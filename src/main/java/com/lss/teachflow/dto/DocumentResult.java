package com.lss.teachflow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentResult extends SearchResult {
    private String title;
    private String url;
    private String source;
    private String description;
    private String type;
    private String typeName;
    private String size;
    private String date;
    private double score;
}