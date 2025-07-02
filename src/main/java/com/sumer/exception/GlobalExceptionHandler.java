package com.sumer.exception;

import com.sumer.dto.Response;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        Response errorResponse = Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Error creating insurance: " + ex.getMessage())
                .totalPage(0)
                .totalElement(0)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException exception, WebRequest request) {
        Response errorResponse = Response.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .totalPage(0)
                .totalElement(0)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Response> handleInvalidCredentialsException(InvalidCredentialsException exception, WebRequest request) {
        Response errorResponse = Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .totalPage(0)
                .totalElement(0)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAllException(Exception ex, WebRequest request) {
        Response errorResponse = Response.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .totalPage(0)
                .totalElement(0)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
