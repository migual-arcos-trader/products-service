package com.marcos.products_service.integration;

import com.marcos.products_service.dto.ProductDTO;
import com.marcos.products_service.exception.ProductNotFoundException;
import com.marcos.products_service.model.Product;
import com.marcos.products_service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class ProductIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private ProductService productService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Test
    void containerShouldBeRunning() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldSaveAndRetrieveProduct() {
        // Arrange
        ProductDTO dto = new ProductDTO("Test Product", 100.0, "Test Description");

        // Act
        Product saved = productService.createProduct(dto);
        Product found = productService.getProductById(saved.getId())
                .orElseThrow();

        // Assert
        assertThat(found.getName()).isEqualTo("Test Product");
        assertThat(found.getPrice()).isEqualTo(100.0);
    }

    @Test
    void shouldFailWhenProductNotFound() {
        assertThatThrownBy(() -> productService.updateProduct(999L, new ProductDTO("None", 0, "")))
                .isInstanceOf(ProductNotFoundException.class);
    }

}