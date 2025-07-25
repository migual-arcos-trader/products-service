package com.marcos.products_service.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void shouldPassValidationWithValidData() {
        ProductDTO productDTO = new ProductDTO("Laptop", 1500.0, "Gaming laptop");

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertTrue(violations.isEmpty(), "There should be no validation errors");
    }

    @Test
    @DisplayName("Should fail validation when name is blank")
    void shouldFailValidationWhenNameIsBlank() {
        ProductDTO productDTO = new ProductDTO("   ", 1200.0, "Description");

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertFalse(violations.isEmpty(), "Validation should fail due to blank name");
        assertEquals("Product name cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should fail validation when price is zero or negative")
    void shouldFailValidationWhenPriceIsNonPositive() {
        ProductDTO productDTO = new ProductDTO("Valid Name", 0.0, "Description");

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertFalse(violations.isEmpty(), "Validation should fail due to non-positive price");
        assertEquals("Product price must be positive", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Should allow null or empty description")
    void shouldAllowNullOrEmptyDescription() {
        ProductDTO productDTOWithNullDescription = new ProductDTO("Product A", 10.0, null);
        ProductDTO productDTOWithEmptyDescription = new ProductDTO("Product B", 20.0, "");

        assertTrue(validator.validate(productDTOWithNullDescription).isEmpty());
        assertTrue(validator.validate(productDTOWithEmptyDescription).isEmpty());
    }

}
