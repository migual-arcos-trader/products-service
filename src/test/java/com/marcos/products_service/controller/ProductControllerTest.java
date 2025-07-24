package com.marcos.products_service.controller;

import com.marcos.products_service.dto.ProductDTO;
import com.marcos.products_service.exception.ProductNotFoundException;
import com.marcos.products_service.model.Product;
import com.marcos.products_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO("Test Product", 100.0, "Test Description");
        product = new Product(1L, "Test Product", 100.0, "Test Description");
    }

    @Test
    @DisplayName("✅ POST /products - Should return 201 CREATED")
    void createProductShouldReturnCreated() {
        when(productService.createProduct(any())).thenReturn(product);

        ResponseEntity<Map<String, Object>> response = productController.createProduct(productDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody())
                .extracting("data.attributes.name")
                .isEqualTo("Test Product");
    }

    @Test
    @DisplayName("✅ GET /products/{id} - Should return 200 OK when product exists")
    void getByIdShouldReturnProductWhenExists() {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        ResponseEntity<Map<String, Object>> response = productController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .extracting("data.attributes.id")
                .isEqualTo(1L);
    }

    @Test
    @DisplayName("❌ GET /products/{id} - Should return 404 when product not found")
    void getByIdShouldReturnNotFound() {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = productController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("✅ PUT /products/{id} - Should return 200 OK when update succeeds")
    void updateShouldReturnOkWhenProductExists() throws ProductNotFoundException {
        when(productService.updateProduct(1L, productDTO)).thenReturn(product);

        ResponseEntity<Map<String, Object>> response = productController.update(1L, productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .extracting("data.attributes.name")
                .isEqualTo("Test Product");
    }

    @Test
    @DisplayName("❌ PUT /products/{id} - Should return 404 when product not found")
    void updateShouldReturnNotFound() throws ProductNotFoundException {
        when(productService.updateProduct(1L, productDTO)).thenThrow(new ProductNotFoundException(1L));

        ResponseEntity<Map<String, Object>> response = productController.update(1L, productDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("✅ DELETE /products/{id} - Should return 204 NO CONTENT")
    void deleteShouldReturnNoContent() throws ProductNotFoundException {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("❌ DELETE /products/{id} - Should return 404 when product not found")
    void deleteShouldReturnNotFound() throws ProductNotFoundException {
        doThrow(new ProductNotFoundException(1L)).when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("✅ GET /products - Should return all products")
    void getAllShouldReturnProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(product));

        ResponseEntity<List<Product>> response = productController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .hasSize(1)
                .first()
                .extracting(Product::getName)
                .isEqualTo("Test Product");
    }

}