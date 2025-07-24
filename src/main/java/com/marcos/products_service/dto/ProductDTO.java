package com.marcos.products_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @Positive(message = "Product price must be positive")
    private double price;

    private String description;

    public ProductDTO(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
