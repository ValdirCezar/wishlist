package br.com.valdircezar.wishlist.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Set;

public record WishlistResponse(

        @Schema(description = "Wishlist id", example = "997f2e1a9f5cf6e2ca4beae3")
        String id,

        @Schema(description = "User id", example = "606b3efb74e106091aae50d8")
        String userId,

        @Schema(description = "List of products ids")
        Set<ProductResponse> products,

        @Schema(description = "Total value of the wishlist", example = "700.0")
        BigDecimal totalValue
) {
}
