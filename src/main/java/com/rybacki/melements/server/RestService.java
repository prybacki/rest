package com.rybacki.melements.server;

import com.rybacki.melements.client.RestClient;
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

    @Value("${github.endpoint}")
    private String githubEndpoint;

    private final RestClient client;

    private final Mapper mapper;

    @Autowired
    public RestService(RestClient client, Mapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public CorrectResponse getRepositoryDetails(String owner, String repositoryName, Locale locale) throws HttpStatusCodeException, ResourceAccessException {
        URI uri = generateURL(owner, repositoryName);
        return mapper.gitHubResponseToRestServiceResponse(client.getRepositoryDetails(uri),
                locale);
    }

    private URI generateURL(String owner, String repositoryName) {
        return UriComponentsBuilder.newInstance()
                .scheme("https").host(githubEndpoint).pathSegment("repos", owner, repositoryName).build().toUri();
    }
}
