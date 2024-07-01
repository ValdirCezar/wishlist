package br.com.valdircezar.wishlist.services;

import br.com.valdircezar.wishlist.clients.ProductClientMock;
import br.com.valdircezar.wishlist.mappers.WishlistMapper;
import br.com.valdircezar.wishlist.models.entities.Wishlist;
import br.com.valdircezar.wishlist.models.exceptions.ObjectNotFoundException;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
import br.com.valdircezar.wishlist.models.requests.ProductRequest;
import br.com.valdircezar.wishlist.repositories.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class WishlistServiceTest {

    @InjectMocks
    private WishlistService wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistMapper wishlistMapper;

    ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest("997f2e1a9f5cf6e2ca4beae4", 1);

    }

    @Test
    @DisplayName("When call save with products size greater than 20 then throw exception")
    void whenCallSave_withProductsSizeGreaterThan20_thenThrowException() {
        var createWishlistRequest = new CreateWishlistRequest("Test save", "606b3efb74e106091aae50d8", Set.of());

        createWishlistRequest.setProducts(IntStream.range(0, 21).mapToObj(i -> {
            var productResponse = ProductClientMock.getProductMockResponse().get(i);
            return new ProductRequest(productResponse.getId(), 1);
        }).collect(Collectors.toSet()));

        try {
            wishlistService.save(createWishlistRequest);
        } catch (Exception e) {
            assertEquals("The wishlist can't have more than 20 products.", e.getMessage());
        }

        verifyNoInteractions(wishlistRepository);
        verifyNoInteractions(wishlistMapper);

        createWishlistRequest.setProducts(Set.of());
    }

    @Test
    @DisplayName("When call save with product id not found then throw exception")
    void whenCallSave_withProductIdNotFound_thenThrowException() {
        var createWishlistRequest = new CreateWishlistRequest("Test save", "606b3efb74e106091aae50d8", Set.of());
        createWishlistRequest.setProducts(Set.of(new ProductRequest("123", 1)));

        try {
            wishlistService.save(createWishlistRequest);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Product not found by id: 123", e.getMessage());
        }

        verifyNoInteractions(wishlistRepository);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call addProduct with user id not found then throw exception")
    void whenCallAddProduct_withUserIdNotFound_thenThrowException() {
        var createWishlistRequest = new CreateWishlistRequest("Test save", "3936", Set.of());
        createWishlistRequest.setProducts(Set.of(productRequest));

        try {
            wishlistService.save(createWishlistRequest);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("User not found by id: 3936", e.getMessage());
        }

        verifyNoInteractions(wishlistRepository);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call save with valid request then save wishlist")
    void whenCallSave_withValidRequest_thenSaveWishlist() {
        var createWishlistRequest = new CreateWishlistRequest("Test save", "606b3efb74e106091aae50d8", Set.of(productRequest));
        var dateNow = LocalDateTime.now();
        var entity = new Wishlist("123", "test", "3936", Set.of(), dateNow);

        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(entity);
        when(wishlistMapper.toEntity(any(CreateWishlistRequest.class))).thenReturn(entity);

        wishlistService.save(createWishlistRequest);

        verify(wishlistRepository).save(entity);
        verify(wishlistMapper).toEntity(any());
    }

}