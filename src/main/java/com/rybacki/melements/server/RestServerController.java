package com.rybacki.melements.server;

import com.rybacki.melements.server.responses.CorrectResponse;
import com.rybacki.melements.server.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import javax.inject.Inject;
import java.util.Locale;

@RequestMapping("/repositories")
@RestController
public class RestServerController {

    private final RestService service;

    @Inject
    public RestServerController(RestService service) {
        this.service = service;
    }

    @GetMapping("/{owner}/{repositoryName}")
    public CorrectResponse getRepositoryDetails(@PathVariable("owner") String owner, @PathVariable("repositoryName") String repositoryName, Locale locale) throws HttpClientErrorException {
        return service.getRepositoryDetails(owner, repositoryName, locale);
    }

    @RequestMapping (value = "/**", method = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE})
    public ResponseEntity<ErrorResponse> handleNotSupportMethod() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), HttpStatus.METHOD_NOT_ALLOWED.value()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> onHttpClientErrorException(HttpClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(new ErrorResponse(ex.getMessage(), ex.getStatusCode().value()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> onResourceAccessException(ResourceAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR ).body(new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }




    //TODO
//            unit test

//                    e2e test
    //integrate test
    //zrobic z endpointa propertisa
    //javadocki
    //opis
//poprawic nazwy metod

    // dorobic test sprawdzajacy z ustawiaja sie locale podane w requescie
    //zmienic null w localach na Optionala
    //testy exceptionow
    //dorobic parametry do testow mappera z localami
    //dodac anotacje sortuajaca correct response zgodnie z wymaganiami
}
