package com.rybacki.melements.server;

import com.rybacki.melements.client.GithubRestClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@Component
public class RestServiceTest {
    private static String TEST_OWNER = "octocat";
    private static String TEST_REPOSITORY_NAME = "boysenberry-repo-1";

    @Mock
    private GithubRestClient client;

    @Mock
    private RestObjectMapper restObjectMapper;

    @Autowired
    @InjectMocks
    private RestService sut;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldReturnCorrectGitHubRepositoryUrl() {
        sut.getRepositoryDetails(TEST_OWNER, TEST_REPOSITORY_NAME, Locale.US);

        ArgumentCaptor<URI> argument = ArgumentCaptor.forClass(URI.class);
        verify(client).getRepositoryDetails(argument.capture());
        assertTrue(argument.getValue().toString().startsWith("https:") && argument.getValue().toString().endsWith(
                "/repos/octocat/boysenberry-repo-1"));
    }
}