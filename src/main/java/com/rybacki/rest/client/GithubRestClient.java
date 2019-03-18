package com.rybacki.rest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class GithubRestClient {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public ResponseEntity<GitHubRepository> getRepositoryDetails(URI uri) throws HttpStatusCodeException,
            ResourceAccessException {
        RequestEntity request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
        return restTemplate.exchange(request, GitHubRepository.class);
    }
}
