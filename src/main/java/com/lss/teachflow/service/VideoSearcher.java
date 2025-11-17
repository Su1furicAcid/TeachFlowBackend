package com.lss.teachflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss.teachflow.dto.VideoResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class VideoSearcher implements Searcher<VideoResult> {

    @Value("${google.custom.api.key}")
    private String googleApiKey;

    @Value("${google.search.engine.id}")
    private String googleSearchEngineId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getType() {
        return "VIDEO";
    }

    @Override
    public List<VideoResult> search(String query, int maxResults, boolean safeSearch) {
        List<VideoResult> allResults = new ArrayList<>(searchGoogleVideos(query, maxResults, safeSearch));
        List<VideoResult> uniqueResults = removeDuplicateVideos(allResults);
        return uniqueResults.stream().limit(maxResults).collect(Collectors.toList());
    }

    private List<VideoResult> searchGoogleVideos(String query, int maxResults, boolean safeSearch) {
        String url = UriComponentsBuilder.fromUriString("https://www.googleapis.com/customsearch/v1")
                .queryParam("key", googleApiKey)
                .queryParam("cx", googleSearchEngineId)
                .queryParam("q", query)
                .queryParam("num", Math.min(maxResults, 10))
                .queryParam("safe", safeSearch ? "active" : "off")
                .queryParam("siteSearch", "bilibili.com")
                .queryParam("siteSearchFilter", "i")
                .toUriString();
        List<VideoResult> videos = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Google Videos Search Response: " + response);
            JsonNode root = objectMapper.readTree(response);
            if (root.has("items")) {
                for (JsonNode item : root.get("items")) {
                    String link = item.path("link").asText("");
                    VideoResult video = VideoResult.builder()
                            .title(item.path("title").asText("无标题"))
                            .url(link)
                            .thumbnail(getGoogleVideoThumbnail(item.path("pagemap")))
                            .source(detectVideoPlatform(link))
                            .duration(extractDurationFromSnippet(item.path("snippet").asText(null)))
                            .description(item.path("snippet").asText(""))
                            .channel(extractChannelInfo(item))
                            .build();
                    videos.add(video);
                }
            }
            return videos.stream().limit(maxResults).collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Google视频搜索失败: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private String detectVideoPlatform(String url) {
        if (url == null || url.isEmpty()) return "other";
        if (url.contains("youtube.com") || url.contains("youtu.be")) return "youtube";
        if (url.contains("bilibili.com")) return "bilibili";
        if (url.contains("youku.com")) return "youku";
        if (url.contains("vimeo.com")) return "vimeo";
        if (url.contains("iqiyi.com")) return "iqiyi";
        if (url.contains("v.qq.com")) return "tencent";
        return "other";
    }

    private String getGoogleVideoThumbnail(JsonNode pagemap) {
        if (pagemap.has("cse_image") && pagemap.get("cse_image").isArray()) {
            return pagemap.get("cse_image").get(0).path("src").asText("");
        }
        return "";
    }


    private String extractDurationFromSnippet(String snippet) {
        if (snippet == null) return null;
        Pattern pattern = Pattern.compile("(\\d+:\\d+(?::\\d+)?)");
        Matcher matcher = pattern.matcher(snippet);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractChannelInfo(JsonNode item) {
        if (item.has("pagemap") && item.get("pagemap").has("person") && item.get("pagemap").get("person").isArray()) {
            return item.get("pagemap").get("person").get(0).path("name").asText("未知频道");
        }
        return "未知频道";
    }

    private String formatBingDuration(String isoDuration) {
        if (isoDuration == null || isoDuration.isEmpty()) return "";
        try {
            Duration duration = Duration.parse(isoDuration);
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            long seconds = duration.toSecondsPart();
            if (hours > 0) {
                return String.format("%d:%02d:%02d", hours, minutes, seconds);
            }
            return String.format("%d:%02d", minutes, seconds);
        } catch (Exception e) {
            return "";
        }
    }

    private String parseBingDate(String dateStr) {
        if (dateStr == null) return null;
        try {
            return ZonedDateTime.parse(dateStr).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatViews(long views) {
        if (views <= 0) return null;
        if (views >= 100_000_000) return String.format("%.1f亿次", views / 100_000_000.0);
        if (views >= 10_000) return String.format("%.1f万次", views / 10_000.0);
        return views + "次";
    }

    private List<VideoResult> removeDuplicateVideos(List<VideoResult> videos) {
        return new ArrayList<>(videos.stream()
                .filter(video -> video.getUrl() != null && !video.getUrl().isEmpty())
                .collect(Collectors.toMap(VideoResult::getUrl, v -> v, (v1, v2) -> v1))
                .values());
    }
}
