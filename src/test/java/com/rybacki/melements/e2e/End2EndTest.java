package com.rybacki.melements.e2e;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybacki.melements.server.responses.CorrectResponse;
import com.rybacki.melements.server.responses.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.client.ExpectedCount.once;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class End2EndTest {

    private static final String FULL_NAME = "octocat/boysenberry-repo-1";
    private static final String DESCRIPTION = "Testing";
    private static final String CLONE_URL = "https://github.com/octocat/boysenberry-repo-1.git";
    private static final Integer STARS = 0;
    private static final String CREATED_AT_US = "May 10, 2018";
    private static final String CREATED_AT_DE = "10.05.2018";

    private static final String MOCK_SERVER_REQUEST_URL = "https://api.github.com/repos/octocat/boysenberry-repo-1";
    private static final String MOCK_SERVER_SUCCESS_RESPONSE = "{\n" +
            "  \"full_name\": \"octocat/boysenberry-repo-1\",\n" +
            "  \"description\": \"Testing\",\n" +
            "  \"created_at\": \"2018-05-10T17:51:29Z\",\n" +
            "  \"clone_url\": \"https://github.com/octocat/boysenberry-repo-1.git\",\n" +
            "  \"stargazers_count\": 0\n" +
            "}\n";

    private static final String REQUEST_URL = "/repositories/octocat/boysenberry-repo-1";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private WebTestClient webClient;

    private MockRestServiceServer mockServer;
    private ObjectMapper jsonMapper;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        jsonMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnCorrectResponse() throws JsonProcessingException {
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withSuccess(MOCK_SERVER_SUCCESS_RESPONSE,
                MediaType.APPLICATION_JSON_UTF8));
        CorrectResponse expectedResponse = new CorrectResponse(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT_US);

        EntityExchangeResult<String> result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().isOk()
                .expectBody(String.class).returnResult();

        mockServer.verify();
        assertEquals(jsonMapper.writeValueAsString(expectedResponse), result.getResponseBody());
    }

    @Test
    public void shouldReturnCorrectResponseWhenIsSpecifiedLocale() throws JsonProcessingException {
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withSuccess(MOCK_SERVER_SUCCESS_RESPONSE, MediaType.APPLICATION_JSON_UTF8));
        CorrectResponse expectedResponse = new CorrectResponse(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT_DE);

        EntityExchangeResult<String> result = webClient.get()
                .uri(REQUEST_URL)
                .header("Accept-Language", "de")
                .exchange().expectStatus().isOk()
                .expectBody(String.class).returnResult();

        mockServer.verify();
        assertEquals(jsonMapper.writeValueAsString(expectedResponse), result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenServerError() throws JsonProcessingException {
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withServerError());
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        EntityExchangeResult<String> result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().is5xxServerError()
                .expectBody(String.class).returnResult();

        mockServer.verify();
        assertEquals(jsonMapper.writeValueAsString(expectedResponse), result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenBadRequest() throws JsonProcessingException  {
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withBadRequest());
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());

        EntityExchangeResult<String> result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().isBadRequest()
                .expectBody(String.class).returnResult();

        mockServer.verify();
        assertEquals(jsonMapper.writeValueAsString(expectedResponse), result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenNotFound() throws JsonProcessingException {
        mockServer.expect(requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.NOT_FOUND));
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.value());

        EntityExchangeResult<String> result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().isNotFound()
                .expectBody(String.class).returnResult();

        mockServer.verify();
        assertEquals(jsonMapper.writeValueAsString(expectedResponse), result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenMethodIsNotSupported() throws JsonProcessingException  {
        mockServer.expect(never(), requestTo(MOCK_SERVER_REQUEST_URL)).andRespond(withBadRequest());
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), HttpStatus.METHOD_NOT_ALLOWED.value());

        EntityExchangeResult<String> result = webClient.post()
                .uri(REQUEST_URL)
                .exchange().expectStatus().is4xxClientError()
                .expectBody(String.class).returnResult();

        mockServer.verify();
        assertEquals(jsonMapper.writeValueAsString(expectedResponse), result.getResponseBody());
    }

    @Test
    public void shouldErrorResponseWhenResourceAccessException() throws JsonProcessingException {
        mockServer.expect(once(), requestTo(MOCK_SERVER_REQUEST_URL)).andExpect(method(HttpMethod.GET)).andRespond((response) -> { throw new ResourceAccessException("Connection reset");});
        ErrorResponse expectedResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        EntityExchangeResult<String> result = webClient.get()
                .uri(REQUEST_URL)
                .exchange().expectStatus().is5xxServerError()
                .expectBody(String.class).returnResult();

        mockServer.verify();
        assertEquals(jsonMapper.writeValueAsString(expectedResponse), result.getResponseBody());
    }
}
