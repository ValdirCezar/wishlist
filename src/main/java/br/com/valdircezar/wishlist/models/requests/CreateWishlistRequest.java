package br.com.valdircezar.wishlist.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateWishlistRequest(

        @NotBlank(message = "Fiel name is required")
        @Size(min = 3, max = 50, message = "Field name must be between 3 and 50 characters")
        @Schema(description = "Wishlist name", example = "My shoes wishlist")
        String name,

        @NotBlank(message = "Field userId is required")
        @Size(min = 14, max = 30, message = "Field userId must be between 14 and 30 characters")
        @Schema(description = "User ID", example = "64bb3bbe319d2b6e43ba05dd")
        String userId,
        List<String> productsId
) { }
