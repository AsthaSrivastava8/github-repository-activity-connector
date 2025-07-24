package com.demo.github_repository_activity_connector.service;

import com.demo.github_repository_activity_connector.dto.CommitInfoResponse;
import com.demo.github_repository_activity_connector.dto.RepoNameResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GithubService {

    private final WebClient webClient;

    public GithubService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<CommitInfoResponse> getCommits(String repoName, int page, int perPage) {
        // Step 1: Call the GitHub API to get the raw list of commits.
        List<Map> rawCommits = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repos/" + repoName + "/commits")
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build())
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();

        if (rawCommits == null) {
            return Collections.emptyList();
        }

        // Step 2: Map the raw data to our CommitInfoResponse DTO.
        return rawCommits.stream()
                .map(this::toCommitInfoResponse)
                .toList();
    }

    public List<RepoNameResponse> getUserRepositories(String username, int page, int perPage) {
        // Step 1: Call the GitHub API to get the raw list of repositories.
        List<Map> rawRepos = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/" + username + "/repos")
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build())
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();

        if (rawRepos == null) {
            return Collections.emptyList();
        }

        // Step 2: Map the raw data to our RepoNameResponse DTO.
        return rawRepos.stream()
                .map(this::toRepoNameResponse)
                .toList();
    }

    /**
     * Converts a raw map from the GitHub API into a CommitInfoResponse DTO.
     */
    @SuppressWarnings("unchecked")
    private CommitInfoResponse toCommitInfoResponse(Map<String, Object> commitData) {
        Map<String, Object> commitDetails = (Map<String, Object>) commitData.get("commit");
        Objects.requireNonNull(commitDetails, "Commit details cannot be null");
        
        Map<String, Object> authorDetails = (Map<String, Object>) commitDetails.get("author");
        Objects.requireNonNull(authorDetails, "Author details cannot be null");

        String message = (String) commitDetails.get("message");
        String timestamp = (String) authorDetails.get("date");
        String author = (String) authorDetails.get("name");

        return new CommitInfoResponse(message, timestamp, author);
    }

    /**
     * Converts a raw map from the GitHub API into a RepoNameResponse DTO.
     */
    private RepoNameResponse toRepoNameResponse(Map<String, Object> repoData) {
        String name = (String) repoData.get("name");
        String url = (String) repoData.get("html_url");
        return new RepoNameResponse(name, url);
    }
} 