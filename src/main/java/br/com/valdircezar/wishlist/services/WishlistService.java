package br.com.valdircezar.wishlist.services;

import br.com.valdircezar.wishlist.clients.ProductClientMock;
import br.com.valdircezar.wishlist.clients.UserClientMock;
import br.com.valdircezar.wishlist.mappers.WishlistMapper;
import br.com.valdircezar.wishlist.models.entities.Wishlist;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
import br.com.valdircezar.wishlist.repositories.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;

    public Wishlist save(CreateWishlistRequest wishlistRequest) {
        UserClientMock.findById(wishlistRequest.userId());
        wishlistRequest.productsId().forEach(ProductClientMock::findById);

        return wishlistRepository.save(wishlistMapper.toEntity(wishlistRequest));
    }

}
