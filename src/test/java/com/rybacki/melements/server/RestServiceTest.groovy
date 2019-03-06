package com.rybacki.melements.server

import com.rybacki.melements.client.GitHubRepository
import com.rybacki.melements.client.GithubRestClient
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification
import spock.lang.Subject

import java.net.URI
import java.util.Locale

import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.*
import static org.mockito.MockitoAnnotations.initMocks

class RestServiceTest extends Specification{
    @Subject def sut

    def TEST_OWNER = "octocat"
    def TEST_REPOSITORY_NAME = "boysenberry-repo-1"
    def GITHUB_ENDPONIT = "api.github.com"

    def responseEntity = Mock(ResponseEntity)
    def client = Stub(GithubRestClient){
        getRepositoryDetails(_) >> responseEntity
    }
    def restObjectMapper  = Mock(RestObjectMapper)

    def setup() {
        sut = new RestService(client, restObjectMapper, GITHUB_ENDPONIT)
    }

    def shouldReturnCorrectGitHubEndpointUrl() {
        given:
        client = Stub(GithubRestClient)
        responseEntity.getStatusCode() >> HttpStatus.OK

        when:
        sut.getRepositoryDetails(TEST_OWNER, TEST_REPOSITORY_NAME, Locale.US)

        then:
        client.getRepositoryDetails(*_) >> { arguments ->
            final URI argumentPeople = arguments[0]
            assert argumentPeople == new URI("https://" + GITHUB_ENDPONIT + "/repos/octocat/boysenberry-repo-1")
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void shouldExceptionWhenIsReturningNoContent() {
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.NO_CONTENT)
        when(client.getRepositoryDetails(any())).thenReturn(responseEntity)

        sut.getRepositoryDetails(TEST_OWNER, TEST_REPOSITORY_NAME, Locale.US)
    }
}