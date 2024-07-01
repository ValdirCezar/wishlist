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
        validateWishlistSize(wishlistRequest.getProducts().size());
        WishlistPreValidation.validateIds(wishlistRequest);
        return wishlistRepository.save(wishlistMapper.toEntity(wishlistRequest));
    }

    public WishlistResponse findById(final String wishlistId) {
        Wishlist entity = find(wishlistId);

        Set<ProductResponse> products = entity.getProducts().stream()
                .map(product -> ProductClientMock.findById(product.getId()).withQuantity(product.getQuantity()))
                .collect(Collectors.toSet());

        BigDecimal totalValue = products.stream()
                .map(product -> product.getUnityPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return wishlistMapper.toResponse(entity, totalValue, products);
    }

    public void addProduct(String wishlistId, AddNewProductRequest productRequest) {
        Wishlist wishlist = find(wishlistId);

        wishlist.getProducts().stream()
                .filter(product -> product.getId().equals(productRequest.id()))
                .findFirst()
                .ifPresentOrElse(
                        product -> {
                            ProductClientMock.findById(productRequest.id());
                            product.incrementQuantity(productRequest.quantity());
                        },
                        () -> wishlist.getProducts().add(new ProductRequest(productRequest.id(), productRequest.quantity()))
                );

        validateWishlistSize(wishlist.getProducts().size());
        wishlistRepository.save(wishlist);
    }

    public ProductResponse findProductById(String wishlistId, String productId) {
        return find(wishlistId)
                .getProducts().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .map(product -> ProductClientMock.findById(product.getId()).withQuantity(product.getQuantity()))
                .orElseThrow(() -> new ObjectNotFoundException("Product with id " + productId + " not found at wishlist."));
    }

    public void removeProduct(String wishlistId, String productId) {
        Wishlist wishlist = find(wishlistId);

        wishlist.getProducts()
                .stream().filter(product -> product.getId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        product -> wishlist.getProducts().remove(product),
                        () -> {
                            throw new ObjectNotFoundException("Product with id " + productId + " not found at wishlist.");
                        }
                );

        wishlistRepository.save(wishlist);
    }

    private Wishlist find(final String id) {
        return wishlistRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Wishlist not found by id: " + id));
    }

    private static void validateWishlistSize(Integer size) {
        if (size > 20) throw new IllegalArgumentException("The wishlist can't have more than 20 products.");
    }
}
