package br.com.valdircezar.wishlist.controllers;

import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "v1/wishlists")
@Tag(name = "Wishlist", description = "Controller responsible for managing wishlists")
public interface WishlistController {

    @Operation(summary = "Create a new wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wishlist created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User or product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    ResponseEntity<Void> create(
            @RequestBody @Valid CreateWishlistRequest wishlistRequest
    );
}
