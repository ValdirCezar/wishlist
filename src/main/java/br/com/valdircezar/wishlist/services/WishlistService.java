package br.com.valdircezar.wishlist.services;

import br.com.valdircezar.wishlist.clients.ProductClientMock;
import br.com.valdircezar.wishlist.mappers.WishlistMapper;
import br.com.valdircezar.wishlist.models.entities.Wishlist;
import br.com.valdircezar.wishlist.models.exceptions.ObjectNotFoundException;
import br.com.valdircezar.wishlist.models.requests.AddNewProductRequest;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
import br.com.valdircezar.wishlist.models.requests.ProductRequest;
import br.com.valdircezar.wishlist.models.responses.ProductResponse;
import br.com.valdircezar.wishlist.models.responses.WishlistResponse;
import br.com.valdircezar.wishlist.repositories.WishlistRepository;
import br.com.valdircezar.wishlist.services.validations.WishlistPreValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;

    public Wishlist save(CreateWishlistRequest wishlistRequest) {
        WishlistPreValidation.applyValidationRules(wishlistRequest);
        return wishlistRepository.save(wishlistMapper.toEntity(wishlistRequest));
    }

    public WishlistResponse findById(final String wishlistId) {
        Wishlist entity = find(wishlistId);

        BigDecimal totalValue = entity.getProducts().stream()
                .map(product -> ProductClientMock.findById(product.getId()).getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Set<ProductResponse> products = entity.getProducts().stream()
                .map(product -> ProductClientMock.findById(product.getId()).withQuantity(product.getQuantity()))
                .collect(Collectors.toSet());

        return wishlistMapper.toResponse(entity, totalValue, products);
    }

    public void addProduct(String wishlistId, AddNewProductRequest productRequest) {
        Wishlist wishlist = find(wishlistId);

        wishlist.getProducts().stream()
                .filter(product -> product.getId().equals(productRequest.id()))
                .findFirst()
                .ifPresentOrElse(
                        product -> product.incrementQuantity(productRequest.quantity()),
                        () -> wishlist.getProducts().add(new ProductRequest(productRequest.id(), productRequest.quantity()))
                );

        wishlistRepository.save(wishlist);
    }

    private Wishlist find(final String id) {
        return wishlistRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Wishlist not found by id: " + id));
    }
}
