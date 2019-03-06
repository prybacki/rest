package com.rybacki.melements.server

import com.rybacki.melements.client.GitHubRepository
import com.rybacki.melements.server.responses.CorrectResponse
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

import static org.junit.Assert.assertEquals

class RestObjectMapperTest extends Specification{

    @Subject def sut = Mappers.getMapper(RestObjectMapper.class)

    def FULL_NAME = "octocat/boysenberry-repo-1"
    def DESCRIPTION = "Testing"
    def CLONE_URL = "https://github.com/octocat/boysenberry-repo-1.git"
    def STARS = 0
    def CREATED_AT = LocalDate.of(2018, 5, 10)

    def EXPECTED_CREATED_AT_GERMANY = "10.05.2018"
    def EXPECTED_CREATED_AT_US = "May 10, 2018"

    def shouldReturnCorrectResponseWhenLocaleIsGermany() {
        given:
        GitHubRepository entity = new GitHubRepository(FULL_NAME, DESCRIPTION, CLONE_URL, STARS,
                CREATED_AT)

        when:
        CorrectResponse expectedResponse = sut.gitHubToRestService(entity, Locale.GERMANY)

        then:
        entity.getFullName() == expectedResponse.getFullName()
        entity.getDescription() == expectedResponse.getDescription()
        entity.getCloneUrl() == expectedResponse.getCloneUrl()
        entity.getStars() == expectedResponse.getStars()
        EXPECTED_CREATED_AT_GERMANY == expectedResponse.getCreatedAt()
    }

    def shouldReturnCorrectResponseWhenLocaleIsUs() {
        given:
        GitHubRepository entity = new GitHubRepository(FULL_NAME, DESCRIPTION, CLONE_URL, STARS,
                CREATED_AT)

        when:
        CorrectResponse expectedResponse = sut.gitHubToRestService(entity, Locale.US)

        then:
        entity.getFullName() == expectedResponse.getFullName()
        entity.getDescription() == expectedResponse.getDescription()
        entity.getCloneUrl() == expectedResponse.getCloneUrl()
        entity.getStars() == expectedResponse.getStars()
        EXPECTED_CREATED_AT_US == expectedResponse.getCreatedAt()
    }
}