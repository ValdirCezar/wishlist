package br.com.valdircezar.wishlist.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Request structure to create a new wishlist")
public class CreateWishlistRequest {

    @NotBlank(message = "Fiel name is required to create a new wishlist")
    @Size(min = 3, max = 50, message = "Field name must be between 3 and 50 characters")
    @Schema(description = "Wishlist name", example = "My Apple wishlist")
    private String name;

    @NotBlank(message = "Field userId is required to create a new wishlist")
    @Size(min = 14, max = 30, message = "Field userId must be between 14 and 30 characters")
    @Schema(description = "User ID", example = "606b3efb74e106091aae50d8")
    private String userId;

    @Builder.Default
    @Schema(description = "List of products")
    private Set<ProductRequest> products = Set.of();
}
