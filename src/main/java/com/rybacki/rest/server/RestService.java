package com.rybacki.rest.server;

import com.rybacki.rest.client.GitHubRepository;
import com.rybacki.rest.client.GithubRestClient;
import com.rybacki.rest.server.responses.CorrectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Locale;

@Service
public class RestService {

    private final GithubRestClient client;
    private final RestObjectMapper restObjectMapper;

    private String githubEndpoint;

    @Autowired
    public RestService(GithubRestClient client, RestObjectMapper restObjectMapper,
                       @Value("${github.endpoint}") String githubEndpoint) {
        this.client = client;
        this.restObjectMapper = restObjectMapper;
        this.githubEndpoint = githubEndpoint;
    }

    CorrectResponse getRepositoryDetails(String owner, String repositoryName, Locale locale) throws HttpStatusCodeException, ResourceAccessException {
        URI uri = UriComponentsBuilder.newInstance().scheme("https").host(githubEndpoint).pathSegment("repos", owner,
                repositoryName).build().toUri();
        ResponseEntity<GitHubRepository> response = client.getRepositoryDetails(uri);

        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.getReasonPhrase());
        }
        return restObjectMapper.gitHubToRestService(response.getBody(), locale);

    }
}
