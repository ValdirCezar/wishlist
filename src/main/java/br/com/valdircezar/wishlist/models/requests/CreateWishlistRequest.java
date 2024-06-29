package br.com.valdircezar.wishlist.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Request structure to create a new wishlist")
public record CreateWishlistRequest(

        @NotBlank(message = "Fiel name is required")
        @Size(min = 3, max = 50, message = "Field name must be between 3 and 50 characters")
        @Schema(description = "Wishlist name", example = "My shoes wishlist")
        String name,

        @NotBlank(message = "Field userId is required")
        @Size(min = 14, max = 30, message = "Field userId must be between 14 and 30 characters")
        @Schema(description = "User ID", example = "606b3efb74e106091aae50d8")
        String userId,

        @Schema(description = "List of products ID", example = "[\"997f2e1a9f5cf6e2ca4beae4\",\"333abc510d2ef9143eefa7f1\"]")
        List<String> productsIds
) { }
