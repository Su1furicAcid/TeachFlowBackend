package com.lss.teachflow.controller;

import com.lss.teachflow.common.ResponseBody;
import com.lss.teachflow.dto.SearchFilter;
import com.lss.teachflow.dto.SearchRequest;
import com.lss.teachflow.dto.SearchResult;
import com.lss.teachflow.service.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "搜索管理", description = "搜索相关接口")
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseBody<?> search(@RequestBody SearchRequest searchRequest) {
        List<SearchFilter> filters = searchRequest.getFilters();
        Map<String, List<?>> results = new HashMap<>();
        filters.forEach(searchFilter -> {
            var searcher = searchService.getSearcher(String.valueOf(searchFilter.getSourceType()));
            if (searcher != null) {
                List<?> searchResult = searcher.search(
                        searchRequest.getQuery(),
                        searchFilter.getMaxResults(),
                        searchFilter.isSafeSearch()
                );
                results.put(searchFilter.getSourceType() + "s", searchResult);
            }
        });
        return ResponseBody.success(results);
    }

}
