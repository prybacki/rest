package com.rybacki.melements.server;

import com.rybacki.melements.client.GitHubRepository;
import com.rybacki.melements.server.responses.CorrectResponse;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class RestObjectMapperTest {

    private static final String FULL_NAME = "octocat/boysenberry-repo-1";
    private static final String DESCRIPTION = "Testing";
    private static final String CLONE_URL = "https://github.com/octocat/boysenberry-repo-1.git";
    private static final Integer STARS = 0;
    private static final LocalDate CREATED_AT = LocalDate.of(2018, 5, 10);

    private static final String EXPECTED_CREATED_AT_GERMANY = "10.05.2018";
    private static final String EXPECTED_CREATED_AT_US = "May 10, 2018";

    private RestObjectMapper sut;

    @Before
    public void setup() {
        sut = Mappers.getMapper(RestObjectMapper.class);
    }

    @Test
    public void shouldReturnCorrectResponseWhenLocaleIsGermany() {
        checkMapping(Locale.GERMANY, EXPECTED_CREATED_AT_GERMANY);
    }

    @Test
    public void shouldReturnCorrectResponseWhenLocaleIsUs() {
        checkMapping(Locale.US, EXPECTED_CREATED_AT_US);
    }

    private void checkMapping(Locale locale, String expectedDate) {
        GitHubRepository entity = new GitHubRepository(FULL_NAME, DESCRIPTION, CLONE_URL, STARS,
                CREATED_AT);

        CorrectResponse expectedResponse = sut.gitHubToRestService(entity, locale);

        assertEquals("Incorrect full name", entity.getFullName(), expectedResponse.getFullName());
        assertEquals("Incorrect description", entity.getDescription(), expectedResponse.getDescription());
        assertEquals("Incorrect clone url", entity.getCloneUrl(), expectedResponse.getCloneUrl());
        assertEquals("Incorrect stars", entity.getStars(), expectedResponse.getStars());
        assertEquals("Incorrect created at", expectedDate,
                expectedResponse.getCreatedAt());
    }
}