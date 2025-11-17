package com.lss.teachflow.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final Map<String, Searcher<?>> searchers;

    public SearchService(List<Searcher<?>> searcherList) {
        this.searchers = searcherList.stream()
                .collect(Collectors.toMap(Searcher::getType, Function.identity()));
    }

    public Searcher<?> getSearcher(String type) {
        return searchers.get(type);
    }
}
