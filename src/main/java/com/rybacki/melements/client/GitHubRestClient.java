package com.rybacki.melements.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GitHubRestClient {

    final static String GITHUB_ENDPOINT = "api.github.com";

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public GitHubRepositoryDetails getRepositoryDetails(String owner, String repositoryName) throws HttpStatusCodeException, ResourceAccessException {
        String url = generateURL(owner, repositoryName);
        return restTemplate.getForObject(url, GitHubRepositoryDetails.class);
    }

    private String generateURL(String owner, String repositoryName) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host(GITHUB_ENDPOINT).pathSegment("repos", owner, repositoryName)
                .build().toUriString();
    }
}
