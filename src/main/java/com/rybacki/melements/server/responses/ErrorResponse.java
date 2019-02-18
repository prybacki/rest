package com.rybacki.melements.server.responses;

public class ErrorResponse {
    private final String message;
    private final int httpCode;

    public ErrorResponse(String message, int httpCode) {
        this.message = message;
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpCode() {
        return httpCode;
    }


}

