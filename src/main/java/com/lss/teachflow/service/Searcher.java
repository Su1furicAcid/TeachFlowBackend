package com.lss.teachflow.service;

import java.util.List;

public interface Searcher<T> {
    String getType();
    List<T> search(String query, int maxResults, boolean safeSearch);
}
