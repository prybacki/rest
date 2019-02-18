package com.rybacki.melements.server;

import com.rybacki.melements.client.GitHubRestClient;
import com.rybacki.melements.server.responses.CorrectResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import javax.inject.Inject;
import java.util.Locale;

@Component
public class RestService {

    private final GitHubRestClient client;

    private final Mapper mapper;

    @Inject
    public RestService(GitHubRestClient client, Mapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public CorrectResponse getRepositoryDetails(String username, String repositoryName, Locale locale) throws HttpClientErrorException, ResourceAccessException {
        return mapper.gitHubResponseToRestServiceResponse(client.getRepositoryDetails(username, repositoryName), locale);
    }
}
