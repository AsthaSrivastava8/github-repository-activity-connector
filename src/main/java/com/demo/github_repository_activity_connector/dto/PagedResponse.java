package com.demo.github_repository_activity_connector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> items;
    private String nextPageUrl;
} 