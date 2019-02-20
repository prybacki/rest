package com.rybacki.melements.server;

import com.rybacki.melements.client.GitHubRepositoryDetails;
import com.rybacki.melements.server.responses.CorrectResponse;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class MapperTest {

    private static final String FULL_NAME = "octocat/boysenberry-repo-1";
    private static final String DESCRIPTION = "Testing";
    private static final String CLONE_URL = "https://github.com/octocat/boysenberry-repo-1.git";
    private static final Integer STARS = 0;

    private static final LocalDate CREATED_AT = LocalDate.of(2018, 05, 10);
    private static final String EXPECTED_CREATED_AT_GERMANY = "10.05.2018";
    private static final String EXPECTED_CREATED_AT_US = "May 10, 2018";
    private static final String EXPECTED_CREATED_AT_FRANCE = "10 mai 2018";

    private MapperImpl sut;

    @Before
    public void setup() {
        sut = new MapperImpl();
    }

    @Test
    public void shouldReturnCorrectResponseWhenLocaleIsGermany() {
        GitHubRepositoryDetails entity = new GitHubRepositoryDetails(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT);

        CorrectResponse expectedResponse = sut.gitHubResponseToRestServiceResponse(entity, Locale.GERMANY);

        assertEquals("Incorrect full name", entity.getFullName(), expectedResponse.getFullName());
        assertEquals("Incorrect description", entity.getDescription(), expectedResponse.getDescription());
        assertEquals("Incorrect clone url", entity.getCloneUrl(), expectedResponse.getCloneUrl());
        assertEquals("Incorrect stars", entity.getStars(), expectedResponse.getStars());
        assertEquals("Incorrect created at (Germany locale)", EXPECTED_CREATED_AT_GERMANY,
                expectedResponse.getCreatedAt());
    }

    @Test
    public void shouldReturnCorrectResponseWhenLocaleIsEs() {
        GitHubRepositoryDetails entity = new GitHubRepositoryDetails(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT);

        CorrectResponse expectedResponse = sut.gitHubResponseToRestServiceResponse(entity, Locale.FRANCE);

        assertEquals("Incorrect full name", entity.getFullName(), expectedResponse.getFullName());
        assertEquals("Incorrect description", entity.getDescription(), expectedResponse.getDescription());
        assertEquals("Incorrect clone url", entity.getCloneUrl(), expectedResponse.getCloneUrl());
        assertEquals("Incorrect stars", entity.getStars(), expectedResponse.getStars());
        assertEquals("Incorrect created at (Germany locale)", EXPECTED_CREATED_AT_FRANCE,
                expectedResponse.getCreatedAt());
    }

    @Test
    public void shouldReturnCorrectResponseWhenLocaleIsNotSet() {
        GitHubRepositoryDetails entity = new GitHubRepositoryDetails(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT);

        CorrectResponse expectedResponse = sut.gitHubResponseToRestServiceResponse(entity, null);

        assertEquals("Incorrect full name", entity.getFullName(), expectedResponse.getFullName());
        assertEquals("Incorrect description", entity.getDescription(), expectedResponse.getDescription());
        assertEquals("Incorrect clone url", entity.getCloneUrl(), expectedResponse.getCloneUrl());
        assertEquals("Incorrect stars", entity.getStars(), expectedResponse.getStars());
        assertEquals("Incorrect created at (US locale)", EXPECTED_CREATED_AT_US,
                expectedResponse.getCreatedAt());
    }
}