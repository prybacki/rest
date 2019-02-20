package com.rybacki.melements.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class GithubRestClient {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public GitHubRepositoryDetails getRepositoryDetails(URI uri) throws HttpStatusCodeException, ResourceAccessException {
        RequestEntity request = RequestEntity
                .get(uri)
                .accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<GitHubRepositoryDetails> result = restTemplate.exchange(request, GitHubRepositoryDetails.class);

        if (result.getStatusCode().value() == HttpStatus.NO_CONTENT.value()) {
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.getReasonPhrase());
        }else{
            return result.getBody();
        }
    }
}
