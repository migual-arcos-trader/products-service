package com.marcos.products_service.service;

import com.marcos.products_service.dto.ProductDTO;
import com.marcos.products_service.exception.ProductNotFoundException;
import com.marcos.products_service.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(ProductDTO productDTO);

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product updateProduct(Long id, ProductDTO productDTO) throws ProductNotFoundException;

    void deleteProduct(Long id) throws ProductNotFoundException;

}
