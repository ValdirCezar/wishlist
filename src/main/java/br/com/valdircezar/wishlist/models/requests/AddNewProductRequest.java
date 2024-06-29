package br.com.valdircezar.wishlist.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddNewProductRequest(
        @Schema(description = "Product id", example = "997f2e1a9f5cf6e2ca4beae3")
        @Size(min = 14, max = 30, message = "Field id must be between 14 and 30 characters")
        @NotBlank(message = "Field id is required to add a new product")
        String id,

        @Schema(description = "Quantity of this product", example = "1")
        Integer quantity
) {
    public AddNewProductRequest {
        if (quantity == null || quantity == 0) quantity = 1;
    }
}