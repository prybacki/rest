package com.rybacki.melements.server;

import com.rybacki.melements.client.GithubRestClient;
import com.rybacki.melements.server.responses.CorrectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Locale;

@Component
public class RestService {

    private final GithubRestClient client;
    private final RestObjectMapper myMapper;

    @Value("${github.endpoint}")
    private String githubEndpoint;

    @Autowired
    public RestService(GithubRestClient client, RestObjectMapper myMapper) {
        this.client = client;
        this.myMapper = myMapper;
    }

    CorrectResponse getRepositoryDetails(String owner, String repositoryName, Locale locale) throws HttpStatusCodeException, ResourceAccessException {
        URI uri = UriComponentsBuilder.newInstance().scheme("https").host(githubEndpoint).pathSegment("repos", owner,
                repositoryName).build().toUri();
        return myMapper.gitHubResponseToRestServiceResponse(client.getRepositoryDetails(uri), locale);
    }
}
