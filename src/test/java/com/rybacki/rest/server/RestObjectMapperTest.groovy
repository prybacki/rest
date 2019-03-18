package com.rybacki.rest.server

import com.rybacki.rest.client.GitHubRepository
import com.rybacki.rest.server.responses.CorrectResponse
import org.mapstruct.factory.Mappers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class RestObjectMapperTest extends Specification {

    @Subject
    def sut = Mappers.getMapper(RestObjectMapper.class)

    def FULL_NAME = "octocat/boysenberry-repo-1"
    def DESCRIPTION = "Testing"
    def CLONE_URL = "https://github.com/octocat/boysenberry-repo-1.git"
    def STARS = 0
    def CREATED_AT = LocalDate.of(2018, 5, 10)

    @Shared
    def EXPECTED_CREATED_AT_GERMANY = "10.05.2018"
    @Shared
    def EXPECTED_CREATED_AT_US = "May 10, 2018"

    def shouldReturnCorrectResponseWhenLocaleIsSet() {
        given:
        GitHubRepository entity = new GitHubRepository(FULL_NAME, DESCRIPTION, CLONE_URL, STARS,
                CREATED_AT)

        when:
        CorrectResponse actual = sut.gitHubToRestService(entity, locale)

        then:
        FULL_NAME == actual.getFullName()
        DESCRIPTION == actual.getDescription()
        CLONE_URL == actual.getCloneUrl()
        STARS == actual.getStars()
        expectedCreatedAt == actual.getCreatedAt()

        where:
        locale         | expectedCreatedAt
        Locale.GERMANY | EXPECTED_CREATED_AT_GERMANY
        Locale.US      | EXPECTED_CREATED_AT_US

    }
}