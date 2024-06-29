package br.com.valdircezar.wishlist.controllers.impl;

import br.com.valdircezar.wishlist.controllers.WishlistController;
import br.com.valdircezar.wishlist.models.entities.Wishlist;
import br.com.valdircezar.wishlist.models.requests.AddNewProductRequest;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
import br.com.valdircezar.wishlist.models.responses.ProductResponse;
import br.com.valdircezar.wishlist.models.responses.WishlistResponse;
import br.com.valdircezar.wishlist.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequiredArgsConstructor
public class WishlistControllerImpl implements WishlistController {

    private final WishlistService wishlistService;

    @Override
    public ResponseEntity<Void> create(CreateWishlistRequest wishlistRequest) {
        Wishlist wishlist = wishlistService.save(wishlistRequest);
        return ResponseEntity.created(
                fromCurrentRequest().path("/{id}").buildAndExpand(wishlist.getId()).toUri()
        ).build();
    }

    @Override
    public ResponseEntity<WishlistResponse> findById(String wishlistId) {
        return ResponseEntity.ok(wishlistService.findById(wishlistId));
    }

    @Override
    public ResponseEntity<Void> addProduct(String wishlistId, AddNewProductRequest productRequest) {
        wishlistService.addProduct(wishlistId, productRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ProductResponse> findProductById(String wishlistId, String productId) {
        return ResponseEntity.ok(wishlistService.findProductById(wishlistId, productId));
    }

    @Override
    public ResponseEntity<Void> removeProduct(String wishlistId, String productId) {
        wishlistService.removeProduct(wishlistId, productId);
        return ResponseEntity.noContent().build();
    }
}
