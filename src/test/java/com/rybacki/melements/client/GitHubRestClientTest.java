package com.rybacki.melements.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class GitHubRestClientTest {

    private static String TEST_OWNER = "octocat";
    private static String TEST_REPOSITORY_NAME = "boysenberry-repo-1";

    @Mock
    private RestTemplate template;

    @Autowired
    @InjectMocks
    private GitHubRestClient client;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldReturnCorrectGitHubRepositoryUrl() {
        try {
            client.getRepositoryDetails(TEST_OWNER, TEST_REPOSITORY_NAME);
        }catch (HttpStatusCodeException ex){
        }


        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(template).getForObject(argument.capture(), ArgumentMatchers.<Class<GitHubRepositoryDetails>>any(),
                any(GitHubRepositoryDetails.class));
        assertEquals("https://" + GitHubRestClient.GITHUB_ENDPOINT + "/repos/octocat/boysenberry-repo-1",
                argument.getValue());
    }
}