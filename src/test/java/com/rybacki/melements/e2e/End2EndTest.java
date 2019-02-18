package com.rybacki.melements.e2e;


import com.rybacki.melements.server.responses.CorrectResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class End2EndTest {

    private static final String FULL_NAME = "octocat/boysenberry-repo-1";
    private static final String DESCRIPTION = "Testing";
    private static final String CLONE_URL = "https://github.com/octocat/boysenberry-repo-1.git";
    private static final Integer STARS = 0;
    private static final String CREATE_AT_US = "May 10, 2018";
    private static final String CREATE_AT_DE = "10.05.2018";

    @Autowired
    private WebTestClient webClient;

    @Test
    public void shouldReturnCorrectResponse(){
        CorrectResponse correctResponse = new CorrectResponse(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATE_AT_US);

        EntityExchangeResult<CorrectResponse> result = webClient.get().uri("/repositories/octocat/boysenberry-repo-1").exchange().expectStatus().isOk()
                .expectBody(CorrectResponse.class).returnResult();

        assertEquals(correctResponse, result.getResponseBody());

    }

    @Test
    public void shouldReturnCorrectResponseWhenIsSpecifiedLocale(){
        CorrectResponse correctResponse = new CorrectResponse(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATE_AT_DE);

        EntityExchangeResult<CorrectResponse> result = webClient.get()
                .uri("/repositories/octocat/boysenberry-repo-1")
                .header("Accept-Language", "de")
                .exchange().expectStatus().isOk()
                .expectBody(CorrectResponse.class).returnResult();

        assertEquals(correctResponse, result.getResponseBody());

    }
}
