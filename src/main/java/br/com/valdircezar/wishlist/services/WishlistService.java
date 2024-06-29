package br.com.valdircezar.wishlist.services;

import br.com.valdircezar.wishlist.clients.ProductClientMock;
import br.com.valdircezar.wishlist.mappers.WishlistMapper;
import br.com.valdircezar.wishlist.models.entities.Wishlist;
import br.com.valdircezar.wishlist.models.exceptions.ObjectNotFoundException;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
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

    public WishlistResponse findById(final String id) {
        Wishlist entity = wishlistRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Wishlist not found by id: " + id));

        BigDecimal totalValue = entity.getProductsIds().stream()
                .map(product -> ProductClientMock.findById(product).price())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Set<ProductResponse> productResponseList = entity.getProductsIds().stream()
                .map(ProductClientMock::findById)
                .collect(Collectors.toSet());

        return wishlistMapper.toResponse(entity, totalValue, productResponseList);
    }


}
