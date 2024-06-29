package br.com.valdircezar.wishlist.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductResponse(
        @Schema(description = "Product id", example = "997f2e1a9f5cf6e2ca4beae3")
        String id,

        @Schema(description = "Product name", example = "Apple Watch")
        String name,

        @Schema(description = "Product price", example = "700.0")
        BigDecimal price
) {
}
