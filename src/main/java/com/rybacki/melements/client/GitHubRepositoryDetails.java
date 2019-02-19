package com.rybacki.melements.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class GitHubRepositoryDetails {

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("clone_url")
    private String cloneUrl;

    @JsonProperty("stargazers_count")
    private Integer stars;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    public GitHubRepositoryDetails(String fullName, String description, String cloneUrl, Integer stars, LocalDate createdAt) {
        this.fullName = fullName;
        this.description = description;
        this.cloneUrl = cloneUrl;
        this.stars = stars;
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public Integer getStars() {
        return stars;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
