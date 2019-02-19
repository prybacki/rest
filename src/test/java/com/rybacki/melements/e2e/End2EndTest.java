package com.rybacki.melements.e2e;


import com.rybacki.melements.server.responses.CorrectResponse;
import com.rybacki.melements.server.responses.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class End2EndTest {

    private static final String FULL_NAME = "octocat/boysenberry-repo-1";
    private static final String DESCRIPTION = "Testing";
    private static final String CLONE_URL = "https://github.com/octocat/boysenberry-repo-1.git";
    private static final Integer STARS = 0;
    private static final String CREATED_AT_US = "May 10, 2018";
    private static final String CREATED_AT_DE = "10.05.2018";

    private static final String RESPONSE = "{\n" +
            "  \"full_name\": \"octocat/boysenberry-repo-1\",\n" +
            "  \"description\": \"Testing\",\n" +
            "  \"created_at\": \"2018-05-10T17:51:29Z\",\n" +
            "  \"clone_url\": \"https://github.com/octocat/boysenberry-repo-1.git\",\n" +
            "  \"stargazers_count\": 0\n" +
            "}\n";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private WebTestClient webClient;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldReturnCorrectResponse() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/boysenberry-repo-1")).andRespond(withSuccess(RESPONSE, MediaType.APPLICATION_JSON_UTF8));

        CorrectResponse expectedResponse = new CorrectResponse(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT_US);
        EntityExchangeResult<CorrectResponse> result = webClient.get()
                .uri("/repositories/octocat/boysenberry-repo-1")
                .exchange().expectStatus().isOk()
                .expectBody(CorrectResponse.class).returnResult();

        mockServer.verify();

        assertEquals(expectedResponse, result.getResponseBody());
    }

    @Test
    public void shouldReturnCorrectResponseWhenIsSpecifiedLocale() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/boysenberry-repo-1")).andRespond(withSuccess(RESPONSE, MediaType.APPLICATION_JSON_UTF8));

        CorrectResponse expectedResponse = new CorrectResponse(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT_DE);
        EntityExchangeResult<CorrectResponse> result = webClient.get()
                .uri("/repositories/octocat/boysenberry-repo-1")
                .header("Accept-Language", "de")
                .exchange().expectStatus().isOk()
                .expectBody(CorrectResponse.class).returnResult();

        mockServer.verify();

        assertEquals(expectedResponse, result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenServerError() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/boysenberry-repo-1")).andRespond(withServerError());
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        EntityExchangeResult<ErrorResponse> result = webClient.get()
                .uri("/repositories/octocat/boysenberry-repo-1")
                .exchange().expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class).returnResult();

        mockServer.verify();

        assertEquals(expectedResponse, result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenBadRequest() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/boysenberry-repo-1")).andRespond(withBadRequest());
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());

        EntityExchangeResult<ErrorResponse> result = webClient.get()
                .uri("/repositories/octocat/boysenberry-repo-1")
                .exchange().expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class).returnResult();

        mockServer.verify();

        assertEquals(expectedResponse, result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenNotFound() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/boysenberry-repo-1")).andRespond(withStatus(HttpStatus.NOT_FOUND));
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.value());

        EntityExchangeResult<ErrorResponse> result = webClient.get()
                .uri("/repositories/octocat/boysenberry-repo-1")
                .exchange().expectStatus().isNotFound()
                .expectBody(ErrorResponse.class).returnResult();

        mockServer.verify();

        assertEquals(expectedResponse, result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenMethodIsNotSupported() {
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), HttpStatus.METHOD_NOT_ALLOWED.value());

        EntityExchangeResult<ErrorResponse> result = webClient.post()
                .uri("/repositories/octocat/boysenberry-repo-1")
                .exchange().expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class).returnResult();

        assertEquals(expectedResponse, result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenResourceAccessException() {
        mockServer.expect(requestTo("https://api.github.com/repos/octocat/boysenberry-repo-1")).andRespond((response) -> { throw new ResourceAccessException("Connection reset");});
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        EntityExchangeResult<ErrorResponse> result = webClient.get()
                .uri("/repositories/octocat/boysenberry-repo-1")
                .exchange().expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class).returnResult();

        assertEquals(expectedResponse, result.getResponseBody());
    }
}
