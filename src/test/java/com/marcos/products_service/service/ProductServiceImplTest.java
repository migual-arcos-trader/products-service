package com.marcos.products_service.service;

import com.marcos.products_service.dto.ProductDTO;
import com.marcos.products_service.exception.ProductNotFoundException;
import com.marcos.products_service.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl();
    }

    // Provide different DTOs for parameterized tests
    static Stream<ProductDTO> productDtoProvider() {
        return Stream.of(
                new ProductDTO("Mouse", 25.0, "Wireless mouse"),
                new ProductDTO("Keyboard", 45.0, "Mechanical keyboard"),
                new ProductDTO("Monitor", 150.0, "24-inch LED monitor")
        );
    }

    @DisplayName("✅ Create product with different inputs")
    @ParameterizedTest(name = "Product: {0}")
    @MethodSource("productDtoProvider")
    void createProductShouldStoreCorrectly(ProductDTO dto) {
        Product product = productService.createProduct(dto);

        assertThat(product).isNotNull();
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo(dto.getName());
        assertThat(product.getPrice()).isEqualTo(dto.getPrice());
        assertThat(product.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    @DisplayName("✅ Retrieve all products")
    void getAllProductsShouldReturnAll() {
        productService.createProduct(new ProductDTO("Mouse", 25.0, "Wireless"));
        productService.createProduct(new ProductDTO("Keyboard", 45.0, "Mechanical"));

        List<Product> products = productService.getAllProducts();

        assertThat(products).hasSize(2);
    }

    @Test
    @DisplayName("✅ Retrieve product by ID should return correct product")
    void getProductByIdShouldReturnCorrectProduct() {
        // Arrange
        Product created = productService.createProduct(new ProductDTO("Monitor", 150.0, "Full HD"));

        // Act
        Optional<Product> found = productService.getProductById(created.getId());

        // Assert
        assertThat(found)
                .isPresent()
                .hasValueSatisfying(product -> {
                    assertThat(product.getName()).isEqualTo("Monitor");
                    assertThat(product.getPrice()).isEqualTo(150.0);
                    assertThat(product.getDescription()).isEqualTo("Full HD");
                });
    }

    @Test
    @DisplayName("✅ Update product details")
    void updateProductShouldModifyFields() {
        Product created = productService.createProduct(new ProductDTO("HDD", 60.0, "1TB"));

        Product updated = productService.updateProduct(created.getId(), new ProductDTO("SSD", 120.0, "512GB"));

        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("SSD");
        assertThat(updated.getPrice()).isEqualTo(120.0);
        assertThat(updated.getDescription()).isEqualTo("512GB");
    }

    @Test
    @DisplayName("✅ Delete product should remove from storage")
    void deleteProductShouldRemoveFromStorage() {
        // Arrange
        ProductDTO testProduct = new ProductDTO("Camera", 250.0, "HD");
        Product created = productService.createProduct(testProduct);

        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> productService.deleteProduct(created.getId()));

        Optional<Product> deletedProduct = productService.getProductById(created.getId());
        assertThat(deletedProduct).isEmpty();
    }

    @Test
    @DisplayName("✅ Concurrent access should not corrupt data")
    void concurrentAccess_shouldBeThreadSafe() throws InterruptedException {
        int threads = 10;
        ExecutorService service = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < threads; i++) {
            service.execute(() -> {
                try {
                    latch.await();
                    productService.createProduct(new ProductDTO("Concurrent", 10.0, "Test"));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        latch.countDown();
        service.shutdown();
        assertThat(service.awaitTermination(1, TimeUnit.SECONDS)).isTrue();
        assertThat(productService.getAllProducts()).hasSize(threads);
    }

    @Test
    @DisplayName("❌ Update non-existent product should throw")
    void updateNonExistentProduct_shouldThrow() {
        ProductDTO dto = new ProductDTO("New", 10.0, "New");
        assertThatThrownBy(() -> productService.updateProduct(999L, dto))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("❌ Delete non-existent product should throw ProductNotFoundException")
    void deleteNonExistentProductShouldThrow() {
        Long nonExistentId = 999L;

        assertThatThrownBy(() -> productService.deleteProduct(nonExistentId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(nonExistentId.toString());
    }

    @Test
    @DisplayName("❌ Get non-existent product should return empty")
    void getNonExistentProductShouldReturnEmpty() {
        Optional<Product> result = productService.getProductById(999L);
        assertThat(result).isEmpty();
    }

}
