package com.rybacki.melements.server;

import com.rybacki.melements.client.GitHubRepositoryDetails;
import com.rybacki.melements.server.responses.CorrectResponse;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;

import java.util.Locale;

import static org.junit.Assert.*;

public class MapperTest {

    private static final String FULL_NAME = "octocat/boysenberry-repo-1";
    private static final String DESCRIPTION = "Testing";
    private static final String CLONE_URL = "https://github.com/octocat/boysenberry-repo-1.git";
    private static final Integer STARS = 0;

    private static final LocalDate CREATED_AT = LocalDate.of(2018,05,10);
    private static final String EXPECTED_CREATED_AT_DE = "10.05.2018";
    private static final String EXPECTED_CREATED_AT_US = "May 10, 2018";

    private MapperImpl sut;

    @Before
    public void setup() {
        sut = new MapperImpl();
    }

    @Test
    public void shouldReturnCorrectResponseWhenLocaleIsEs() {
        GitHubRepositoryDetails entity = new GitHubRepositoryDetails(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT);
        Locale locale = new Locale.Builder().setLanguage("de").setRegion("DE").build();

        CorrectResponse correctResponse = sut.gitHubResponseToRestServiceResponse(entity, locale);

        assertEquals("Incorrect full name", entity.getFullName(), correctResponse.getFullName() );
        assertEquals("Incorrect description", entity.getDescription(), correctResponse.getDescription() );
        assertEquals("Incorrect clone url", entity.getCloneUrl(), correctResponse.getCloneUrl() );
        assertEquals("Incorrect stars", entity.getStars(), correctResponse.getStars() );
        assertEquals("Incorrect created at", EXPECTED_CREATED_AT_DE, correctResponse.getCreatedAt() );
    }

    @Test
    public void shouldReturnCorrectResponseWhenLocaleIsNotSet() {
        // given
        GitHubRepositoryDetails entity = new GitHubRepositoryDetails(FULL_NAME, DESCRIPTION, CLONE_URL, STARS, CREATED_AT);

        // when
        CorrectResponse correctResponse = sut.gitHubResponseToRestServiceResponse(entity, null);

        // then
        assertEquals("Incorrect full name", entity.getFullName(), correctResponse.getFullName() );
        assertEquals("Incorrect description",  entity.getDescription(), correctResponse.getDescription() );
        assertEquals("Incorrect clone url", entity.getCloneUrl(), correctResponse.getCloneUrl() );
        assertEquals("Incorrect stars", entity.getStars(), correctResponse.getStars() );
        assertEquals("Incorrect created at", EXPECTED_CREATED_AT_US, correctResponse.getCreatedAt() );
    }
}