package com.rybacki.melements.server.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"fullName", "description", "cloneUrl", "stars", "createdAt"})
public class CorrectResponse {

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("cloneUrl")
    private String cloneUrl;

    @JsonProperty("stars")
    private Integer stars;

    @JsonProperty("createdAt")
    private String createdAt;

}
