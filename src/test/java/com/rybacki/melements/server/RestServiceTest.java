package com.rybacki.melements.server;

import com.rybacki.melements.client.GitHubRepository;
import com.rybacki.melements.client.GithubRestClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RestServiceTest {
    private static String TEST_OWNER = "octocat";
    private static String TEST_REPOSITORY_NAME = "boysenberry-repo-1";
    private static String GITHUB_ENDPONIT = "api.github.com";

    private RestService sut;

    @Mock
    ResponseEntity<GitHubRepository> responseEntity;
    @Mock
    private GithubRestClient client;
    @Mock
    private RestObjectMapper restObjectMapper;

    @Before
    public void setUp() {
        initMocks(this);
        sut = new RestService(client, restObjectMapper, GITHUB_ENDPONIT);
    }

    @Test
    public void shouldReturnCorrectGitHubEndpointUrl() {
        when(client.getRepositoryDetails(any())).thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);

        sut.getRepositoryDetails(TEST_OWNER, TEST_REPOSITORY_NAME, Locale.US);

        ArgumentCaptor<URI> argument = ArgumentCaptor.forClass(URI.class);
        verify(client).getRepositoryDetails(argument.capture());
        assertEquals("https://" + GITHUB_ENDPONIT + "/repos/octocat/boysenberry-repo-1",
                argument.getValue().toString());
    }

    @Test(expected = HttpClientErrorException.class)
    public void shouldExceptionWhenClientReturnNoContent() {
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.NO_CONTENT);
        when(client.getRepositoryDetails(any())).thenReturn(responseEntity);

        sut.getRepositoryDetails(TEST_OWNER, TEST_REPOSITORY_NAME, Locale.US);
    }
}