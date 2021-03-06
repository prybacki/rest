package com.rybacki.rest.server;

import com.rybacki.rest.server.responses.CorrectResponse;
import com.rybacki.rest.server.responses.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Locale;

@RequestMapping("/repositories")
@RestController
public class RestServerController {

    private final RestService service;

    @Autowired
    public RestServerController(RestService service) {
        this.service = service;
    }

    @GetMapping("/{owner}/{repositoryName}")
    public CorrectResponse getRepositoryDetails(@PathVariable("owner") String owner,
                                                @PathVariable("repositoryName") String repositoryName, Locale locale) throws HttpClientErrorException {
        return service.getRepositoryDetails(owner, repositoryName, locale);
    }

    @RequestMapping(value = "/**")
    public ResponseEntity<ErrorResponse> onNotSupportedMethod() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), HttpStatus.METHOD_NOT_ALLOWED.value()));
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<ErrorResponse> onHttpClientErrorException(HttpStatusCodeException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(new ErrorResponse(ex.getStatusCode().getReasonPhrase(),
                ex.getStatusCode().value()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> onResourceAccessException() {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse(HttpStatus.BAD_GATEWAY.getReasonPhrase(), HttpStatus.BAD_GATEWAY.value()));
    }
}
