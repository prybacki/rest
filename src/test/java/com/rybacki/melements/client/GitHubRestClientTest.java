package com.rybacki.melements.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class GitHubRestClientTest {

    private static String TEST_OWNER = "octocat";
    private static String TEST_REPOSITORY_NAME = "boysenberry-repo-1";

    @Mock
    private RestTemplate template;

    @Mock
    private RestTemplateBuilder builder;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldReturnCorrectGItHubRepositoryUrl() {
        when(builder.build()).thenReturn(template);
        GitHubRestClient client = Mockito.spy(new GitHubRestClient(builder));

        client.getRepositoryDetails(TEST_OWNER, TEST_REPOSITORY_NAME);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(template).getForObject(argument.capture(), ArgumentMatchers.<Class<GitHubRepositoryDetails>>any(), any(GitHubRepositoryDetails.class));
        assertEquals("https://" + client.GITHUB_ENDPOINT + "/repos/octocat/boysenberry-repo-1", argument.getValue());
    }
}