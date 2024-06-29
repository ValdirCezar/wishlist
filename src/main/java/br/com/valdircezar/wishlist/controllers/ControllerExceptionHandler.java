package br.com.valdircezar.wishlist.controllers;

import br.com.valdircezar.wishlist.models.exceptions.ObjectNotFoundException;
import br.com.valdircezar.wishlist.models.exceptions.StandardError;
import br.com.valdircezar.wishlist.models.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    ResponseEntity<StandardError> handleNotFoundException(
            final ObjectNotFoundException ex, final HttpServletRequest request
    ) {
        log.error("Object not found exception: {}", ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(
                StandardError.builder()
                        .timestamp(now())
                        .status(NOT_FOUND.value())
                        .error(NOT_FOUND.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(InternalError.class)
    ResponseEntity<StandardError> handleInternalError(
            final InternalError ex, final HttpServletRequest request
    ) {
        log.error("Internal error: {}", ex.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                StandardError.builder()
                        .timestamp(now())
                        .status(INTERNAL_SERVER_ERROR.value())
                        .error(INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message("We are sorry, but an internal error occurred. Please try again later.")
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<StandardError> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException ex, final HttpServletRequest request
    ) {
        log.error("Validation exception: {}", ex.getMessage());
        var error = ValidationException.builder()
                .timestamp(now())
                .status(BAD_REQUEST.value())
                .error("Validation Exception")
                .message("Error on payload validation attributes")
                .path(request.getRequestURI())
                .errors(new ArrayList<>())
                .build();

        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            error.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(error);
    }
}
