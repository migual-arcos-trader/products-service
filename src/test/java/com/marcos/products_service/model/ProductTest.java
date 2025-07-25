package com.marcos.products_service.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @DisplayName("✅ Product creation with valid data should set fields correctly")
    @ParameterizedTest
    @CsvSource({
            "1, Laptop, 1500.99, High-end gaming laptop",
            "2, Mouse, 25.50, Wireless",
            "3, Monitor, 300.00, 4K Resolution",
            "4, Keyboard, 0.01, ''",
            "5, Headphones, 199.99, null"
    })
    void productCreationWithValidDataShouldSetFields(
            Long id, String name, double price, String description
    ) {
        Product product = new Product(id, name, price, description);

        assertAll(
                () -> assertEquals(id, product.getId(), "ID mismatch"),
                () -> assertEquals(name, product.getName(), "Name mismatch"),
                () -> assertEquals(price, product.getPrice(), 0.001, "Price mismatch"),
                () -> assertEquals(description, product.getDescription(), "Description mismatch")
        );
    }

    @DisplayName("✅ Product builder should work correctly")
    @Test
    void productBuilderShouldWork() {
        Product product = Product.builder()
                .name("Test")
                .price(10.0)
                .description("Desc")
                .build();

        assertNotNull(product);
    }

    @DisplayName("❌ Product creation with invalid name should throw IllegalArgumentException")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void productCreationWithInvalidNameShouldThrow(String invalidName) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Product(1L, invalidName, 100.00, "Description"),
                "Expected exception for invalid name: '" + invalidName + "'"
        );

        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("❌ Product creation with invalid price should throw IllegalArgumentException")
    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -999.99, 0.00})
    void productCreationWithInvalidPriceShouldThrow(double invalidPrice) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Product(1L, "ValidName", invalidPrice, "Description"),
                "Expected exception for invalid price: " + invalidPrice
        );

        assertEquals("Product price must be positive", exception.getMessage());
    }


    @DisplayName("✅ Product equality should depend only on ID")
    @ParameterizedTest
    @CsvSource({
            "1, Laptop, 1500.99, 1, Laptop, 1500.99, true",
            "2, Laptop, 1500.99, 2, Laptop, 1500.99, true"
    })
    void productEqualityShouldDependOnId(
            Long id1, String name1, double price1,
            Long id2, String name2, double price2,
            boolean expectedEqual
    ) {
        Product product1 = new Product(id1, name1, price1, "Description");
        Product product2 = new Product(id2, name2, price2, "Description");

        assertEquals(expectedEqual, product1.equals(product2));
        if (expectedEqual) {
            assertEquals(product1.hashCode(), product2.hashCode());
        }
    }


    @DisplayName("✅ Product not equal should depend only on ID")
    @ParameterizedTest
    @CsvSource({
            "1, Laptop, 1500.99, 2, Laptop, 1500.99, true",
            "3, Mouse, 25.50, 4, Laptop, 1500.99, true"
    })
    void productNotEqualShouldDependOnId(
            Long id1, String name1, double price1,
            Long id2, String name2, double price2,
            boolean expectedNotEqual
    ) {
        Product product1 = new Product(id1, name1, price1, "Description");
        Product product2 = new Product(id2, name2, price2, "Description");

        assertNotEquals(expectedNotEqual, product1.equals(product2));
        if (expectedNotEqual) {
            assertNotEquals(product1.hashCode(), product2.hashCode());
        }
    }

    @DisplayName("✅ Product toString should include all fields")
    @ParameterizedTest
    @CsvSource({
            "1, Laptop, 1500.99, 'High-end gaming laptop'",
            "2, Mouse, 25.50, ''"
    })
    void productToStringShouldContainAllFields(
            Long id, String name, double price, String description
    ) {
        Product product = new Product(id, name, price, description);
        String toStringOutput = product.toString();

        assertAll(
                () -> assertTrue(toStringOutput.contains(id.toString()), "toString() should to include the ID."),
                () -> assertTrue(toStringOutput.contains(name), "toString() should to include the name."),
                () -> assertTrue(toStringOutput.contains(String.valueOf(price)), "toString() should to include the price."),
                () -> {
                    if (Objects.nonNull(description) && !description.isEmpty()) {
                        assertTrue(toStringOutput.contains(description), "toString() should to include the description.");
                    } else {
                        assertFalse(toStringOutput.contains("null"), "toString() should to include 'null' if the description is null/empty.");
                    }
                }
        );
    }

}