package com.marcos.products_service.controller;

import com.marcos.products_service.dto.ProductDTO;
import com.marcos.products_service.exception.ProductNotFoundException;
import com.marcos.products_service.model.Product;
import com.marcos.products_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildJsonApiResponse(createdProduct));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(buildJsonApiResponse(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO) {
        try {
            Product updated = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(buildJsonApiResponse(updated));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, Object> buildJsonApiResponse(Product product) {
        Map<String, Object> attributes = Map.of(
                "id", product.getId(),
                "name", product.getName(),
                "price", product.getPrice(),
                "description", product.getDescription()
        );

        Map<String, Object> data = Map.of(
                "type", "product",
                "attributes", attributes
        );

        return Map.of("data", data);
    }
}
