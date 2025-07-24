package com.marcos.products_service.exception;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    @InjectMocks
    private RestExceptionHandler exceptionHandler;

    @Test
    @DisplayName("Should handle validation errors according to JSON API")
    void handleValidationErrors() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError(
                "productDTO",
                "name",
                "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<Map<String, Object>> response =
                exceptionHandler.handleValidationErrors(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(response.getBody())
                .extracting("errors")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .first()
                .asInstanceOf(MAP)
                .containsEntry("detail", "must not be blank")
                .containsEntry("source", Map.of("pointer", "/data/attributes/name"));
    }

    @Test
    @DisplayName("Should handle ProductNotFoundException")
    void handleProductNotFound() {
        // Arrange
        ProductNotFoundException ex = new ProductNotFoundException(1L);

        // Act
        ResponseEntity<Map<String, Object>> response =
                exceptionHandler.handleProductNotFound(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody())
                .extracting("errors")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .first()
                .asInstanceOf(MAP)
                .containsEntry("detail", "Product with id 1 not found")
                .containsEntry("source", Map.of("pointer", "/data/id"));
    }

    @Test
    @DisplayName("Should handle generic exceptions")
    void handleGenericException() {
        // Arrange
        Exception ex = new Exception("Test error");

        // Act
        ResponseEntity<Map<String, Object>> response =
                exceptionHandler.handleGeneralExceptions(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertThat(response.getBody())
                .extracting("errors")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .first()
                .asInstanceOf(MAP)
                .containsEntry("title", "Internal Server Error");
    }
}
