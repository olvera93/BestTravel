package com.olvera.best_travel.api.controllers.error_handler;

import com.olvera.best_travel.api.model.responses.BaseErrorResponse;
import com.olvera.best_travel.api.model.responses.ErrorResponse;
import com.olvera.best_travel.util.exceptions.ForbiddenCustomerException;
import com.olvera.best_travel.util.exceptions.IdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenCustomerHandler {

    @ExceptionHandler(ForbiddenCustomerException.class)
    public BaseErrorResponse handleForbiddenClient(ForbiddenCustomerException exception) {
        return ErrorResponse.builder()
                .error(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.name())
                .errorCode(HttpStatus.FORBIDDEN.value())
                .build();
    }
}
