package br.com.valdircezar.wishlist.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@With
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    @Schema(description = "Product id", example = "997f2e1a9f5cf6e2ca4beae3")
    private String id;

    @Schema(description = "Product name", example = "Apple Watch")
    private String name;

    @Schema(description = "Product quantity", example = "1")
    private Integer quantity;

    @Schema(description = "Product price", example = "700.0")
    private BigDecimal price;
}
