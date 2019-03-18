package com.rybacki.rest.server.responses;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String message;
    private final int httpCode;

    public ErrorResponse(String message, int httpCode) {
        this.message = message;
        this.httpCode = httpCode;
    }
}

