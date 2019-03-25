package com.rybacki.rest.server


import com.rybacki.rest.client.GithubRestClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import spock.lang.FailsWith
import spock.lang.Specification
import spock.lang.Subject

class RestServiceTest extends Specification {
    @Subject
    def sut

    def TEST_OWNER = "octocat"
    def TEST_REPOSITORY_NAME = "boysenberry-repo-1"
    def GITHUB_ENDPONIT = "api.github.com"

    def responseEntity = Mock(ResponseEntity)
    def client = Stub(GithubRestClient) {
        getRepositoryDetails(_) >> responseEntity
    }
    def restObjectMapper = Mock(RestObjectMapper)

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

    @FailsWith(HttpClientErrorException)
    def shouldExceptionWhenIsReturningNoContent() {
        given:
        responseEntity.getStatusCode() >> HttpStatus.NO_CONTENT

        expect:
        sut.getRepositoryDetails(TEST_OWNER, TEST_REPOSITORY_NAME, Locale.US)
    }
}