package com.lss.teachflow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResult extends SearchResult {
    private String title;
    private String url;
    private String thumbnail;
    private String source;
    private int width;
    private int height;
    private String size;
    private String format;
}
