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

@org.mapstruct.Mapper(componentModel="spring")
public interface Mapper {

    Locale defaultLocale = new Locale.Builder().setLanguage("en").setRegion("US").build();

    @Mappings({
            @Mapping(source = "fullName", target = "fullName"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "cloneUrl", target = "cloneUrl"),
            @Mapping(source = "stars", target = "stars"),
            @Mapping(target = "createdAt", expression = "java(convertDate(gitHubRepositoryDetails.getCreatedAt(), locale))")
    })
    CorrectResponse gitHubResponseToRestServiceResponse(GitHubRepositoryDetails gitHubRepositoryDetails, @Context Locale locale);

    default String convertDate(LocalDate gitHubDate, Locale locale) {
        if (locale == null){
            locale = defaultLocale;
        }
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
        return gitHubDate.format(pattern);
    }
}
