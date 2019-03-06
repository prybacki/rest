package com.rybacki.melements.e2e

import com.fasterxml.jackson.databind.ObjectMapper
import com.rybacki.melements.server.responses.CorrectResponse
import com.rybacki.melements.server.responses.ErrorResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.test.web.client.ExpectedCount.never
import static org.springframework.test.web.client.ExpectedCount.once
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class End2EndTest extends Specification {

    def FULL_NAME = "octocat/boysenberry-repo-1"
    def DESCRIPTION = "Testing"
    def CLONE_URL = "https://github.com/octocat/boysenberry-repo-1.git"
    def STARS = 0
    def CREATED_AT_US = "May 10, 2018"
    def CREATED_AT_DE = "10.05.2018"

    def MOCK_SERVER_REQUEST_URL = "https://api.github.com/repos/octocat/boysenberry-repo-1"
    def MOCK_SERVER_SUCCESS_RESPONSE = "{\n" +
            "  \"full_name\": \"octocat/boysenberry-repo-1\",\n" +
            "  \"description\": \"Testing\",\n" +
            "  \"created_at\": \"2018-05-10T17:51:29Z\",\n" +
            "  \"clone_url\": \"https://github.com/octocat/boysenberry-repo-1.git\",\n" +
            "  \"stargazers_count\": 0\n" +
            "}\n"

    def REQUEST_URL = "/repositories/octocat/boysenberry-repo-1"

    @Autowired
    RestTemplate restTemplate

    @Autowired
    private WebTestClient webClient

    def mockServer
    def jsonMapper

    def setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
        jsonMapper = new ObjectMapper()
    }

    def shouldReturnCorrectResponse() {
        given:
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withSuccess(MOCK_SERVER_SUCCESS_RESPONSE,
                MediaType.APPLICATION_JSON_UTF8))
        def expectedResponse = new CorrectResponse(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT_US)

        when:
        def result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().isOk()
                .expectBody(String.class).returnResult()

        then:
        mockServer.verify()
        jsonMapper.writeValueAsString(expectedResponse) == result.getResponseBody()
    }

    def shouldReturnCorrectResponseWhenIsSpecifiedLocale() {
        given:
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withSuccess(MOCK_SERVER_SUCCESS_RESPONSE, MediaType.APPLICATION_JSON_UTF8))
        def expectedResponse = new CorrectResponse(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT_DE)

        when:
        def result = webClient.get()
                .uri(REQUEST_URL)
                .header("Accept-Language", "de")
                .exchange().expectStatus().isOk()
                .expectBody(String.class).returnResult()

        then:
        mockServer.verify()
        jsonMapper.writeValueAsString(expectedResponse) == result.getResponseBody()
    }

    def shouldErrorResponseWhenServerError() {
        given:
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withServerError())
        def expectedResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.value())

        when:
        def result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().is5xxServerError()
                .expectBody(String.class).returnResult()

        then:
        mockServer.verify()
        jsonMapper.writeValueAsString(expectedResponse) == result.getResponseBody()
    }

    def shouldErrorResponseWhenBadRequest() {
        given:
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withBadRequest())
        def expectedResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value())

        when:
        def result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().isBadRequest()
                .expectBody(String.class).returnResult()

        then:
        mockServer.verify()
        jsonMapper.writeValueAsString(expectedResponse) == result.getResponseBody()
    }

    def shouldErrorResponseWhenNotFound() {
        given:
        mockServer.expect(requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.NOT_FOUND))
        def expectedResponse = new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.value())

        when:
        def result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().isNotFound()
                .expectBody(String.class).returnResult()

        then:
        mockServer.verify()
        jsonMapper.writeValueAsString(expectedResponse) == result.getResponseBody()
    }

    def shouldErrorResponseWhenMethodIsNotSupported() {
        given:
        mockServer.expect(never(), requestTo(MOCK_SERVER_REQUEST_URL)).andRespond(withBadRequest())
        def expectedResponse = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                HttpStatus.METHOD_NOT_ALLOWED.value())

        when:
        def result = webClient.post()
                .uri(REQUEST_URL)
                .exchange().expectStatus().is4xxClientError()
                .expectBody(String.class).returnResult()

        then:
        mockServer.verify()
        jsonMapper.writeValueAsString(expectedResponse) == result.getResponseBody()
    }

    def shouldErrorResponseWhenResourceAccessException() {
        given:
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond({
            x -> throw new ResourceAccessException("Connection reset")
        })
        def expectedResponse = new ErrorResponse(HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                HttpStatus.BAD_GATEWAY.value())

        when:
        def result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().is5xxServerError()
                .expectBody(String.class).returnResult()

        then:
        mockServer.verify()
        jsonMapper.writeValueAsString(expectedResponse) == result.getResponseBody()
    }

    def shouldErrorResponseWhenNoContent() {
        given:
        mockServer.expect(requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.NO_CONTENT))

        when:
        def result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().isNoContent().expectBody().returnResult()

        then:
        mockServer.verify()
        result.getResponseBody() == null
    }

    def shouldCorrectResponseIsInProperOrder() {
        given:
        def expectedResponse = "{\"fullName\":\"octocat/boysenberry-repo-1\"," +
                "\"description\":\"Testing\"," +
                "\"cloneUrl\":\"https://github.com/octocat/boysenberry-repo-1.git\"," +
                "\"stars\":0," +
                "\"createdAt\":\"May 10, 2018\"}"
        mockServer.expect(requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withSuccess(MOCK_SERVER_SUCCESS_RESPONSE,
                MediaType.APPLICATION_JSON_UTF8))

        when:
        def result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().isOk()
                .expectBody(String.class).returnResult()

        then:
        mockServer.verify()
        expectedResponse == result.getResponseBody()
    }
}
