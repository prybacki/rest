package com.rybacki.melements.server;

import com.rybacki.melements.client.GitHubRepositoryDetails;
import com.rybacki.melements.server.responses.CorrectResponse;
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
    abstract CorrectResponse gitHubResponseToRestServiceResponse(GitHubRepositoryDetails gitHubRepositoryDetails,
                                                        @Context Locale locale);

    @AfterMapping
    void formatCreatedAt(GitHubRepositoryDetails gitHubRepositoryDetails,
                         @MappingTarget CorrectResponse correctResponse,
                         @Context Locale locale) {
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
        correctResponse.setCreatedAt(gitHubRepositoryDetails.getCreatedAt().format(pattern));
    }
}
