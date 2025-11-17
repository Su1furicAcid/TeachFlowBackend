package com.lss.teachflow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoResult extends SearchResult {
    private String title;
    private String url;
    private String thumbnail;
    private String source;
    private String duration;
    private String views;
    private String uploadDate;
    private String channel;
    private String description;
}
