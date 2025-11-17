package com.lss.teachflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss.teachflow.dto.ImageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ImageSearcher implements Searcher<ImageResult> {

    @Value("${google.custom.api.key}")
    private String googleApiKey;

    @Value("${google.search.engine.id}")
    private String googleSearchEngineId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getType() {
        return "IMAGE";
    }

    @Override
    public List<ImageResult> search(String query, int maxResults, boolean safeSearch) {
        return new ArrayList<>(searchImagesGoogle(query, maxResults, safeSearch));
    }

    private List<ImageResult> searchImagesGoogle(String query, int maxResults, boolean safeSearch) {
        String url = UriComponentsBuilder.fromUriString("https://www.googleapis.com/customsearch/v1")
                .queryParam("key", googleApiKey)
                .queryParam("cx", googleSearchEngineId)
                .queryParam("q", query)
                .queryParam("searchType", "image")
                .queryParam("num", Math.min(maxResults, 10))
                .queryParam("safe", safeSearch ? "active" : "off")
                .toUriString();
        List<ImageResult> images = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Google Images Search Response: " + response);
            JsonNode root = objectMapper.readTree(response);
            if (root.has("items")) {
                for (JsonNode item : root.get("items")) {
                    JsonNode imageNode = item.path("image");
                    ImageResult img = ImageResult.builder()
                            .title(item.path("title").asText("无标题"))
                            .url(item.path("link").asText(""))
                            .thumbnail(imageNode.path("thumbnailLink").asText(""))
                            .source(extractDomain(imageNode.path("contextLink").asText("")))
                            .width(imageNode.path("width").asInt(0))
                            .height(imageNode.path("height").asInt(0))
                            .format(extractImageFormat(item.path("link").asText("")))
                            .size(formatImageSize(imageNode.path("byteSize").asLong(0)))
                            .build();
                    images.add(img);
                }
            }
            return images.stream().limit(maxResults).collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Google图片搜索失败: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private String extractImageFormat(String url) {
        if (url == null || url.isEmpty()) return "unknown";
        Pattern pattern = Pattern.compile("\\.(jpe?g|png|gif|bmp|webp|tiff?)(?:[?#]|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : "unknown";
    }

    private String extractDomain(String url) {
        if (url == null || url.isEmpty()) return "未知来源";
        return url.replaceAll("^https?://(www\\.)?", "").replaceAll("/.*$", "");
    }

    private String formatImageSize(long sizeBytes) {
        if (sizeBytes <= 0) {
            return "未知大小";
        }
        String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(sizeBytes) / Math.log10(1024));
        return String.format("%.1f%s", sizeBytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}
