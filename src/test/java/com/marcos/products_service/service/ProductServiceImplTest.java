package com.marcos.products_service.service;

import com.marcos.products_service.dto.ProductDTO;
import com.marcos.products_service.exception.ProductNotFoundException;
import com.marcos.products_service.model.Product;
import com.marcos.products_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(100.0)
                .description("Test Description")
                .build();
    }

    @Test
    @DisplayName("✅ Create product should save and return product")
    void createProductShouldStoreCorrectly() {
        // Arrange
        ProductDTO dto = new ProductDTO("Mouse", 25.0, "Wireless");
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productService.createProduct(dto);

        // Assert
        assertThat(result).isNotNull();
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("✅ Get all products should return product list")
    void getAllProductsShouldReturnAll() {
        // Arrange
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("✅ Get product by ID should return product")
    void getProductByIdShouldReturnProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Optional<Product> result = productService.getProductById(1L);

        // Assert
        assertThat(result)
                .isPresent()
                .contains(testProduct);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("✅ Update product should modify fields")
    void updateProductShouldModifyFields() throws ProductNotFoundException {
        // Arrange
        ProductDTO updateDto = new ProductDTO("Updated", 150.0, "New Desc");
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Product result = productService.updateProduct(1L, updateDto);

        // Assert
        assertThat(result.getName()).isEqualTo("Updated");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("✅ Delete product should call repository")
    void deleteProductShouldCallRepository() throws ProductNotFoundException {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertThatNoException()
                .isThrownBy(() -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

}