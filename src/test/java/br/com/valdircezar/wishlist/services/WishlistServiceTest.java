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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class WishlistServiceTest {

    public static final String ID_123 = "123";
    public static final String WISHLIST_NAME = "My wishlist";
    public static final String VALID_USER_ID = "606b3efb74e106091aae50d8";

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
        var createWishlistRequest = new CreateWishlistRequest(WISHLIST_NAME, VALID_USER_ID, Set.of());

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
        var createWishlistRequest = new CreateWishlistRequest(WISHLIST_NAME, VALID_USER_ID, Set.of());
        createWishlistRequest.setProducts(Set.of(new ProductRequest(ID_123, 1)));

        try {
            wishlistService.save(createWishlistRequest);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Product not found by id: " + ID_123, e.getMessage());
        }

        verifyNoInteractions(wishlistRepository);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call addProduct with user id not found then throw exception")
    void whenCallAddProduct_withUserIdNotFound_thenThrowException() {
        var createWishlistRequest = new CreateWishlistRequest(WISHLIST_NAME, "3936", Set.of());
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
        var createWishlistRequest = new CreateWishlistRequest(WISHLIST_NAME, VALID_USER_ID, Set.of(productRequest));

        var dateNow = LocalDateTime.now();
        var entity = new Wishlist(ID_123, "test", VALID_USER_ID, Set.of(), dateNow);

        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(entity);
        when(wishlistMapper.toEntity(any(CreateWishlistRequest.class))).thenReturn(entity);

        wishlistService.save(createWishlistRequest);

        verify(wishlistRepository).save(entity);
        verify(wishlistMapper).toEntity(any());
    }

    @Test
    @DisplayName("When call findById with wishlist id not found then throw exception")
    void whenCallFindById_withWishlistIdNotFound_thenThrowException() {
        when(wishlistRepository.findById(ID_123)).thenReturn(Optional.empty());

        try {
            wishlistService.findById(ID_123);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Wishlist not found by id: 123", e.getMessage());
        }

        verify(wishlistRepository).findById(ID_123);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call findById with valid request then return wishlist")
    void whenCallFindById_withValidRequest_thenReturnWishlist() {
        var entity = new Wishlist(ID_123, WISHLIST_NAME, VALID_USER_ID, Set.of(), LocalDateTime.now());
        var response = new WishlistResponse(ID_123, WISHLIST_NAME, VALID_USER_ID, Set.of(), BigDecimal.ZERO);

        when(wishlistRepository.findById(ID_123)).thenReturn(Optional.of(entity));
        when(wishlistMapper.toResponse(any(), any(), any())).thenReturn(response);

        var result = wishlistService.findById(ID_123);

        assertEquals(response, result);

        verify(wishlistRepository).findById(ID_123);
        verify(wishlistMapper).toResponse(entity, BigDecimal.ZERO, Set.of());
    }

    @Test
    @DisplayName("When call findById with valid request then return correct wishlist total value")
    void whenCallFindById_withValidRequest_thenReturnCorrectWishlistTotalValue() {
        var wishlist = new Wishlist("333", WISHLIST_NAME, VALID_USER_ID, Set.of(), LocalDateTime.now());

        wishlist.setProducts(IntStream.range(0, 3).mapToObj(i -> {
            var productResponse = ProductClientMock.getProductMockResponse().get(i);
            return new ProductRequest(productResponse.getId(), 1);
        }).collect(Collectors.toSet()));

        when(wishlistRepository.findById(anyString())).thenReturn(Optional.of(wishlist));
        when(wishlistMapper.toResponse(any(), any(), any())).thenReturn(new WishlistResponse("333", WISHLIST_NAME, VALID_USER_ID, any(), BigDecimal.ZERO));

        var result = wishlistService.findById("333");

        // Nesse ponto garantimos que o valor total da wishlist response é a soma dos 3 primeiros produtos do mock
        verify(wishlistMapper).toResponse(eq(wishlist), eq(BigDecimal.valueOf(1101.0)), any());
        verify(wishlistRepository).findById("333");

        assertNotNull(result);
        assertEquals(WishlistResponse.class, result.getClass());
        assertEquals("333", result.id());
        assertEquals(WISHLIST_NAME, result.name());
    }

    @Test
    @DisplayName("When call addProduct with wishlist id not found then throw exception")
    void whenCallAddProduct_withWishlistIdNotFound_thenThrowException() {
        when(wishlistRepository.findById(ID_123)).thenReturn(Optional.empty());

        try {
            wishlistService.addProduct(ID_123, new AddNewProductRequest(ID_123, 1));
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Wishlist not found by id: 123", e.getMessage());
        }

        verify(wishlistRepository).findById(ID_123);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call addProduct with product id not found then throw exception")
    void whenCallAddProduct_withProductIdNotFound_thenThrowException() {
        var wishlist = new Wishlist(ID_123, WISHLIST_NAME, VALID_USER_ID, Set.of(), LocalDateTime.now());

        when(wishlistRepository.findById(anyString())).thenReturn(Optional.of(wishlist));

        try {
            wishlistService.addProduct(ID_123, new AddNewProductRequest(ID_123, 1));
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Product not found by id: 123", e.getMessage());
        }

        verify(wishlistRepository).findById(ID_123);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call addProduct with valid request and product already exists then increment quantity")
    void whenCallAddProduct_withValidRequestAndProductAlreadyExists_thenIncrementQuantity() {
        var wishlist = new Wishlist(ID_123, WISHLIST_NAME, VALID_USER_ID, Set.of(productRequest), LocalDateTime.now());

        when(wishlistRepository.findById(anyString())).thenReturn(Optional.of(wishlist));

        // Quantidade antes das operação
        int quantityBefore = wishlist.getProducts().iterator().next().getQuantity();

        Set<ProductResponse> productsResponses = wishlist.getProducts().stream()
                .map(product -> ProductClientMock.getProductMockResponse().getFirst().withQuantity(product.getQuantity()))
                .collect(Collectors.toSet());

        var response = wishlistMapper.toResponse(wishlist, BigDecimal.TEN, productsResponses);
        when(wishlistMapper.toResponse(any(Wishlist.class), any(BigDecimal.class), anySet())).thenReturn(response);

        wishlistService.addProduct(ID_123, new AddNewProductRequest("997f2e1a9f5cf6e2ca4beae4", 1));

        // Quantidade após a operação
        int quantityAfter = wishlist.getProducts().iterator().next().getQuantity();

        // Valida quantidade
        assertEquals(quantityBefore + 1, quantityAfter);

        verify(wishlistRepository).findById(ID_123);
        verify(wishlistRepository).save(wishlist);
        verify(wishlistMapper).toResponse(wishlist, BigDecimal.TEN, productsResponses);
    }

    @Test
    @DisplayName("When call addProduct with valid request and product not exists at wishlist then add product")
    void whenCallAddProduct_withValidRequestAndProductNotExistsAtWishlist_thenAddProduct() {
        var wishlist = new Wishlist(ID_123, WISHLIST_NAME, VALID_USER_ID, new HashSet<>(), LocalDateTime.now());

        when(wishlistRepository.findById(anyString())).thenReturn(Optional.of(wishlist));

        Set<ProductResponse> productsResponses = wishlist.getProducts().stream()
                .map(product -> ProductClientMock.getProductMockResponse().getFirst().withQuantity(product.getQuantity()))
                .collect(Collectors.toSet());

        var response = wishlistMapper.toResponse(wishlist, BigDecimal.TEN, productsResponses);
        when(wishlistMapper.toResponse(any(Wishlist.class), any(BigDecimal.class), anySet())).thenReturn(response);

        wishlistService.addProduct(ID_123, new AddNewProductRequest("336b3efb74e106091aae50d2", 1));

        // Valida se o produto foi adicionado
        assertEquals(1, wishlist.getProducts().size());

        verify(wishlistRepository).findById(ID_123);
        verify(wishlistRepository).save(wishlist);
        verify(wishlistMapper).toResponse(wishlist, BigDecimal.TEN, productsResponses);
    }

    @Test
    @DisplayName("When call addProduct with valid request and wishlist has 20 products then throw exception")
    void whenCallAddProduct_withValidRequestAndWishlistHas20Products_thenThrowException() {
        var wishlist = new Wishlist(ID_123, WISHLIST_NAME, VALID_USER_ID, new HashSet<>(), LocalDateTime.now());

        wishlist.setProducts(IntStream.range(0, 20).mapToObj(i -> {
            var productResponse = ProductClientMock.getProductMockResponse().get(i);
            return new ProductRequest(productResponse.getId(), 1);
        }).collect(Collectors.toSet()));

        when(wishlistRepository.findById(anyString())).thenReturn(Optional.of(wishlist));

        try {
            wishlistService.addProduct(ID_123, new AddNewProductRequest("336b3efb74e106091aae50d4", 1));
        } catch (Exception e) {
            assertEquals("The wishlist can't have more than 20 products.", e.getMessage());
        }

        verify(wishlistRepository).findById(ID_123);
        verifyNoMoreInteractions(wishlistRepository);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call findProductById with wishlist id not found then throw exception")
    void whenCallFindProductById_withWishlistIdNotFound_thenThrowException() {
        when(wishlistRepository.findById(anyString())).thenReturn(Optional.empty());

        try {
            wishlistService.findProductById(ID_123, ID_123);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Wishlist not found by id: 123", e.getMessage());
        }

        verify(wishlistRepository).findById(ID_123);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call findProductById with product valid id then return product")
    void whenCallFindProductById_withProductValidId_thenReturnProduct() {
        var wishlist = new Wishlist(ID_123, WISHLIST_NAME, VALID_USER_ID, Set.of(productRequest), LocalDateTime.now());

        when(wishlistRepository.findById(anyString())).thenReturn(Optional.of(wishlist));

        Set<ProductResponse> productsResponses = wishlist.getProducts().stream()
                .map(product -> ProductClientMock.getProductMockResponse().getFirst().withQuantity(product.getQuantity()))
                .collect(Collectors.toSet());

        var response = wishlistMapper.toResponse(wishlist, BigDecimal.TEN, productsResponses);
        when(wishlistMapper.toResponse(any(Wishlist.class), any(BigDecimal.class), anySet())).thenReturn(response);

        var result = wishlistService.findProductById(ID_123, "997f2e1a9f5cf6e2ca4beae4");

        verify(wishlistRepository).findById(ID_123);
        verify(wishlistMapper).toResponse(wishlist, BigDecimal.TEN, productsResponses);

        assertNotNull(result);
        assertEquals(ProductResponse.class, result.getClass());
    }

    @Test
    @DisplayName("When call removeProduct with wishlist id not found then throw exception")
    void whenCallRemoveProduct_withWishlistIdNotFound_thenThrowException() {
        when(wishlistRepository.findById(anyString())).thenReturn(Optional.empty());

        try {
            wishlistService.removeProduct(ID_123, ID_123);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Wishlist not found by id: 123", e.getMessage());
        }

        verify(wishlistRepository).findById(ID_123);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call removeProduct with product id not found then throw exception")
    void whenCallRemoveProduct_withProductIdNotFound_thenThrowException() {
        var wishlist = new Wishlist(ID_123, WISHLIST_NAME, VALID_USER_ID, Set.of(productRequest), LocalDateTime.now());

        when(wishlistRepository.findById(anyString())).thenReturn(Optional.of(wishlist));

        try {
            wishlistService.removeProduct(ID_123, ID_123);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Product with id 123 not found at wishlist.", e.getMessage());
        }

        verify(wishlistRepository).findById(ID_123);
        verifyNoMoreInteractions(wishlistRepository);
        verifyNoInteractions(wishlistMapper);
    }

    @Test
    @DisplayName("When call removeProduct with valid request then remove product and validate wishlist size")
    void whenCallRemoveProduct_withValidRequest_thenRemoveProductAndValidateWishlistSize() {
        var wishlist = new Wishlist(ID_123, WISHLIST_NAME, VALID_USER_ID, new HashSet<>(), LocalDateTime.now());
        wishlist.getProducts().add(productRequest);

        when(wishlistRepository.findById(anyString())).thenReturn(Optional.of(wishlist));

        wishlistService.removeProduct(ID_123, productRequest.getId());

        // Valida se o produto foi removido
        assertEquals(0, wishlist.getProducts().size());

        verify(wishlistRepository).findById(ID_123);
        verify(wishlistRepository).save(wishlist);
    }

}