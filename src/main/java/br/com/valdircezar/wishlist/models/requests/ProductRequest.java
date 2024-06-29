package br.com.valdircezar.wishlist.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @Schema(description = "Product id", example = "997f2e1a9f5cf6e2ca4beae3")
    @Size(min = 14, max = 30, message = "Field id must be between 14 and 30 characters")
    private String id;

    @Schema(description = "Quantity of this product", example = "1")
    private Integer quantity;

    public void incrementQuantity(Integer quantity) {
        this.quantity += quantity;
    }
}
