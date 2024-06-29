package br.com.valdircezar.wishlist.controllers;

import br.com.valdircezar.wishlist.models.exceptions.StandardError;
import br.com.valdircezar.wishlist.models.requests.AddNewProductRequest;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
import br.com.valdircezar.wishlist.models.responses.ProductResponse;
import br.com.valdircezar.wishlist.models.responses.WishlistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "v1/wishlists")
@Tag(name = "WishlistController", description = "Controller responsável por gerenciar as Wishlists")
public interface WishlistController {

    @Operation(summary = "Criar uma nova Wishlist")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Wishlist created",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Not found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            ),
            @ApiResponse(
                    responseCode = "422", description = "Unprocessable entity",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            )
    })
    @PostMapping
    ResponseEntity<Void> create(
            @RequestBody @Valid CreateWishlistRequest wishlistRequest
    );

    @Operation(summary = "Consultar todos os produtos da Wishlist do cliente pelo id da Wishlist")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Wishlist found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = WishlistResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Wishlist not found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            )
    })
    @GetMapping(value = "/{wishlistId}")
    ResponseEntity<WishlistResponse> findById(
            @Parameter(description = "Wishlist id", example = "997f2e1a9f5cf6e2ca4beae3")
            @PathVariable(name = "wishlistId") String wishlistId
    );

    @Operation(summary = "Adicionar um produto na Wishlist")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Product added",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = WishlistResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Not found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            ),
            @ApiResponse(
                    responseCode = "422", description = "Unprocessable entity",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            )
    })
    @PatchMapping(value = "/{wishlistId}/add-product")
    ResponseEntity<Void> addProduct(
            @Parameter(description = "Wishlist id", example = "997f2e1a9f5cf6e2ca4beae3")
            @PathVariable(name = "wishlistId") String wishlistId,
            @RequestBody @Valid AddNewProductRequest productRequest
    );

    @Operation(summary = "Consulta se um produto está presente na Wishlist do cliente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Product found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = WishlistResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = StandardError.class))
            )
    })
    @GetMapping(value = "/{wishlistId}/products/{productId}")
    ResponseEntity<ProductResponse> findProductById(
            @Parameter(description = "Wishlist id", example = "997f2e1a9f5cf6e2ca4beae3")
            @PathVariable(name = "wishlistId") String wishlistId,
            @Parameter(description = "Product id", example = "997f2e1a9f5cf6e2ca4beae3")
            @PathVariable(name = "productId") String productId
    );
}
