package com.marcos.products_service.service;

import com.marcos.products_service.dto.ProductDTO;
import com.marcos.products_service.exception.ProductNotFoundException;
import com.marcos.products_service.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductServiceImpl implements ProductService {

    private final Map<Long, Product> productMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Product createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .build();
        long id = idGenerator.getAndIncrement();
        product.setId(id);
        productMap.put(id, product);
        return product;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productMap.values());
    }

    public Optional<Product> getProductById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws ProductNotFoundException {
        return Optional.ofNullable(productMap.get(id))
                .map(existing -> {
                    Product updated = existing.toBuilder()
                            .name(productDTO.getName())
                            .price(productDTO.getPrice())
                            .description(productDTO.getDescription())
                            .build();
                    productMap.put(id, updated);
                    return updated;
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {
        if (Objects.isNull(productMap.remove(id))) {
            throw new ProductNotFoundException(id);
        }
    }
}
