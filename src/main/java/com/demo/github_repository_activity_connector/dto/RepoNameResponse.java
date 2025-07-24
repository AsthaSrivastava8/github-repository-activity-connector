package com.demo.github_repository_activity_connector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RepoNameResponse {
    private String name;
    private String url;
} 