package com.marcos.products_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> Map.of(
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "title", "Validation Error",
                        "detail", Objects.requireNonNull(fieldError.getDefaultMessage()),
                        "source", Map.of("pointer", "/data/attributes/" + fieldError.getField())
                ))
                .toList();

        return ResponseEntity.badRequest()
                .body(Map.of("errors", errors));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException ex) {
        Map<String, Object> error = Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "title", "Resource Not Found",
                "detail", ex.getMessage(),
                "source", Map.of("pointer", "/data/id")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("errors", List.of(error)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralExceptions(Exception ex) {
        Map<String, Object> error = Map.of(
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "title", "Internal Server Error",
                "detail", ex.getLocalizedMessage()
        );

        return ResponseEntity.internalServerError()
                .body(Map.of("errors", List.of(error)));
    }
}