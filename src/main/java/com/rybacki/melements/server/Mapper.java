package com.rybacki.melements.server;

import com.rybacki.melements.client.GitHubRepositoryDetails;
import com.rybacki.melements.server.responses.CorrectResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    @Mappings({
            @Mapping(source = "fullName", target = "fullName"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "cloneUrl", target = "cloneUrl"),
            @Mapping(source = "stars", target = "stars"),
            @Mapping(target = "createdAt", expression = "java(formatDate(gitHubRepositoryDetails.getCreatedAt(), " +
                    "locale))")
    })
    CorrectResponse gitHubResponseToRestServiceResponse(GitHubRepositoryDetails gitHubRepositoryDetails,
                                                        @Context Locale locale);

    default String formatDate(LocalDate gitHubDate, Locale locale) {
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
        return gitHubDate.format(pattern);
    }
}
