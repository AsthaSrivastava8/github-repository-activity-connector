package com.demo.github_repository_activity_connector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommitInfoResponse {
    private String message;
    private String timestamp;
    private String author;
} 