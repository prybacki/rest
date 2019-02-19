package com.rybacki.melements.server.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    public CorrectResponse(String fullName, String description, String cloneUrl, Integer stars, String createdAt) {
        this.fullName = fullName;
        this.description = description;
        this.cloneUrl = cloneUrl;
        this.stars = stars;
        this.createdAt = createdAt;
    }

    public CorrectResponse() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CorrectResponse that = (CorrectResponse) o;
        return Objects.equals(fullName, that.fullName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(cloneUrl, that.cloneUrl) &&
                Objects.equals(stars, that.stars) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, description, cloneUrl, stars, createdAt);
    }

    @Override
    public String toString() {
        return "CorrectResponse{" +
                "fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                ", cloneUrl='" + cloneUrl + '\'' +
                ", stars=" + stars +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
