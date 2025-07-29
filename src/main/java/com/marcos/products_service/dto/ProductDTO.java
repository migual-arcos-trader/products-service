package com.marcos.products_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Data Transfer Object for product operations")
public class ProductDTO {

    @Schema(description = "Name of the product",
            example = "Premium Wireless Headphones",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @Schema(description = "Price of the product in USD",
            example = "199.99",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive(message = "Product price must be positive")
    private double price;

    @Schema(description = "Detailed description of the product",
            example = "Noise-cancelling wireless headphones with 30-hour battery life")
    private String description;

    public ProductDTO(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}