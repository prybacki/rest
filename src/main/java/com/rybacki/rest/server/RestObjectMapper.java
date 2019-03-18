package com.rybacki.rest.server;

import com.rybacki.rest.client.GitHubRepository;
import com.rybacki.rest.server.responses.CorrectResponse;
import org.mapstruct.*;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Mapper(componentModel = "spring")
public abstract class RestObjectMapper {

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "cloneUrl", target = "cloneUrl")
    @Mapping(source = "stars", target = "stars")
    abstract CorrectResponse gitHubToRestService(GitHubRepository gitHubRepository,
                                                 @Context Locale locale);

    @AfterMapping
    void formatCreatedAt(GitHubRepository gitHubRepository,
                         @MappingTarget CorrectResponse correctResponse,
                         @Context Locale locale) {
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
        correctResponse.setCreatedAt(gitHubRepository.getCreatedAt().format(pattern));
    }
}
