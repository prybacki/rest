package com.rybacki.melements.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GitHubRestClient {

//    @Value("${github.endpoint}")
    final static String GITHUB_ENDPOINT = "api.github.com";

    private final RestTemplate restTemplate;

    public GitHubRestClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public GitHubRepositoryDetails getRepositoryDetails(String owner, String repositoryName) throws HttpClientErrorException, ResourceAccessException {
        String url = generateURL(owner, repositoryName);
        return restTemplate.getForObject(url, GitHubRepositoryDetails.class, new GitHubRepositoryDetails());
    }

    private String generateURL(String owner, String repositoryName) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host(GITHUB_ENDPOINT).pathSegment("repos", owner, repositoryName)
                .build().toUriString();
    }
}
