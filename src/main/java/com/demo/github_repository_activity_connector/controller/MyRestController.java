package com.demo.github_repository_activity_connector.controller;

import com.demo.github_repository_activity_connector.dto.*;
import com.demo.github_repository_activity_connector.service.GithubService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
public class MyRestController {

    private final GithubService githubService;

    public MyRestController(GithubService githubService) {
        this.githubService = githubService;
    }

    @PostMapping("/commits")
    public ResponseEntity<PagedResponse<CommitInfoResponse>> getCommits(@RequestBody RepoRequest repoRequest,
                                                                         @PageableDefault(size = 5) Pageable pageable) {
        int page = pageable.getPageNumber() + 1;
        int size = pageable.getPageSize();
        List<CommitInfoResponse> items = githubService.getCommits(
            repoRequest.getRepoName(),
            page,
            size
        );
        return ResponseEntity.ok(new PagedResponse<>(items, buildNextPageUrl(pageable)));
    }

    @PostMapping("/user-repos")
    public ResponseEntity<PagedResponse<RepoNameResponse>> getUserRepositories(@RequestBody UserRequest userRequest,
                                                                                @PageableDefault(size = 5) Pageable pageable) {
        int page = pageable.getPageNumber() + 1;
        int size = pageable.getPageSize();
        List<RepoNameResponse> items = githubService.getUserRepositories(
            userRequest.getUsername(),
            page,
            size
        );
        return ResponseEntity.ok(new PagedResponse<>(items, buildNextPageUrl(pageable)));
    }

    private String buildNextPageUrl(Pageable pageable) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replaceQueryParam("page", pageable.getPageNumber() + 1)
                .replaceQueryParam("size", pageable.getPageSize())
                .build().toUriString();
    }
} 