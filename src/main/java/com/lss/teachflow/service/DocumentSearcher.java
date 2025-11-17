package com.lss.teachflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss.teachflow.dto.DocumentResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DocumentSearcher implements Searcher<DocumentResult> {
    @Value("${google.custom.api.key}")
    private String googleApiKey;

    @Value("${google.search.engine.id}")
    private String googleSearchEngineId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getType() {
        return "DOCUMENT";
    }

    @Override
    public List<DocumentResult> search(String query, int maxResults, boolean safeSearch) {
        List<DocumentResult> allResults = new ArrayList<>(searchGoogleDocs(query, maxResults, safeSearch));
        List<DocumentResult> uniqueResults = removeDuplicateDocs(allResults);
        uniqueResults.sort(Comparator.comparing(DocumentResult::getScore).reversed());
        return uniqueResults;
    }

    private List<DocumentResult> searchGoogleDocs(String query, int maxResults, boolean safeSearch) {
        String url = UriComponentsBuilder.fromUriString("https://www.googleapis.com/customsearch/v1")
                .queryParam("key", googleApiKey)
                .queryParam("cx", googleSearchEngineId)
                .queryParam("q", query)
                .queryParam("num", Math.min(maxResults, 10))
                .queryParam("safe", safeSearch ? "active" : "off")
                .queryParam("sort", "date")
                .queryParam("lr", "lang_zh-CN")
                .queryParam("fileType", "pdf")
                .toUriString();
        List<DocumentResult> docs = new ArrayList<>();
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Google Docs Search Response: " + response);
            JsonNode root = objectMapper.readTree(response);
            if (root.has("items")) {
                for (JsonNode item : root.get("items")) {
                    String link = item.path("link").asText("");
                    String docType = extractFileType(link);
                    DocumentResult doc = DocumentResult.builder()
                            .title(item.path("title").asText("无标题"))
                            .url(link)
                            .source(extractDomain(link))
                            .description(item.path("snippet").asText("无描述"))
                            .type(docType)
                            .typeName("Adobe PDF")
                            .size(extractSizeInfo(item.path("snippet").asText("")))
                            .date(extractGoogleDate(item))
                            .score(item.path("score").asDouble(0))
                            .build();
                    docs.add(doc);
                }
            }
            return docs.stream().limit(maxResults).collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Google文档搜索失败: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private String extractFileType(String url) {
        if (url == null || url.isEmpty()) return "unknown";
        Pattern pattern = Pattern.compile("\\.([a-z0-9]{2,4})(?:[?#]|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1).toLowerCase() : "unknown";
    }

    private String extractDomain(String url) {
        if (url == null || url.isEmpty()) return "未知来源";
        return url.replaceAll("^https?://(www\\.)?", "").replaceAll("/.*$", "");
    }

    private String extractSizeInfo(String text) {
        if (text == null) return "未知大小";
        Pattern pattern = Pattern.compile("((\\d+\\.?\\d*)\\s*(KB|MB|GB|B))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "未知大小";
    }

    private String extractGoogleDate(JsonNode item) {
        String snippet = item.path("snippet").asText("");
        Pattern pattern = Pattern.compile("(\\d{4}年\\d{1,2}月\\d{1,2}日)|(\\d{4}-\\d{2}-\\d{2})");
        Matcher matcher = pattern.matcher(snippet);
        if (matcher.find()) {
            String dateStr = matcher.group(0);
            try {
                DateTimeFormatter formatter = dateStr.contains("年")
                        ? DateTimeFormatter.ofPattern("yyyy年M月d日")
                        : DateTimeFormatter.ISO_LOCAL_DATE;
                return LocalDate.parse(dateStr, formatter).format(DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                // Ignore parsing errors
            }
        }
        return null;
    }

    private List<DocumentResult> removeDuplicateDocs(List<DocumentResult> docs) {
        return new ArrayList<>(docs.stream()
                .filter(doc -> doc.getUrl() != null && !doc.getUrl().isEmpty())
                .collect(Collectors.toMap(DocumentResult::getUrl, doc -> doc, (doc1, doc2) -> doc1))
                .values());
    }
}
