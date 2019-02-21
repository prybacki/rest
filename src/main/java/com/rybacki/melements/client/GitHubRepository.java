package com.rybacki.melements.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class GitHubRepository {

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
}
