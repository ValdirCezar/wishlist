package br.com.valdircezar.wishlist.controllers;

import br.com.valdircezar.wishlist.models.entities.Wishlist;
import br.com.valdircezar.wishlist.models.exceptions.ObjectNotFoundException;
import br.com.valdircezar.wishlist.models.requests.AddNewProductRequest;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
import br.com.valdircezar.wishlist.models.responses.WishlistResponse;
import br.com.valdircezar.wishlist.services.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static br.com.valdircezar.wishlist.stub.WishlistStub.generateMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WishlistControllerTest {

    private static final String BASE_URI = "/v1/wishlists";
    private static final String USER_ID = "938abc610dcef9143eefa7f3";
    private static final String WISHLIST_ID = "319abc510dcef9143eefa733";
    public static final String WISHLIST_NAME = "teste";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("When call create method, with invalid params, then return business exception")
    void whenCall_createMethodWithInvalidParams_thenReturnBusinessException() throws Exception {
        CreateWishlistRequest request = new CreateWishlistRequest();

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error on payload validation attributes"))
                .andExpect(jsonPath("$.error").value("Validation Exception"))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='Fiel name is required to create a new wishlist')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='userId' && @.message=='Field userId is required to create a new wishlist')]").exists());
    }

    @Test
    @DisplayName("When call create method, with valid params, then return created status")
    void whenCall_createMethodWithValidParams_thenReturnCreatedStatus() throws Exception {
        CreateWishlistRequest request = new CreateWishlistRequest(WISHLIST_NAME, USER_ID, Set.of());

        Wishlist entity = new Wishlist(WISHLIST_ID, WISHLIST_NAME, USER_ID, null, null);
        when(wishlistService.save(any(CreateWishlistRequest.class))).thenReturn(entity);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(header().string("location", "http://localhost/v1/wishlists/" + entity.getId()));
    }

    @Test
    @DisplayName("When call findById method, with valid id, then return wishlist response")
    void whenCall_findByIdMethodWithValidId_thenReturnWishlistResponse() throws Exception {
        WishlistResponse response = generateMock(WishlistResponse.class);
        when(wishlistService.findById(anyString())).thenReturn(response);

        mockMvc.perform(
                        get(BASE_URI + "/" + WISHLIST_ID)
                                .contentType(APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.userId").value(response.userId()))
                .andExpect(jsonPath("$.totalValue").value(response.totalValue()))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products").isNotEmpty());

    }

    @Test
    @DisplayName("When call findById method, with invalid id, then return not found status")
    void whenCall_findByIdMethodWithInvalidId_thenReturnNotFoundStatus() throws Exception {
        when(wishlistService.findById(anyString())).thenThrow(new ObjectNotFoundException("Wishlist not found"));

        mockMvc.perform(
                        get(BASE_URI + "/123")
                                .contentType(APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Wishlist not found"))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/123"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("When call addProduct method, with valid wishlistId and valid product request, then return no content status")
    void whenCall_addProductMethodWithValidWishlistIdAndValidProductRequest_thenReturnNoContentStatus() throws Exception {
        AddNewProductRequest request = new AddNewProductRequest(WISHLIST_ID, 1);
        doNothing().when(wishlistService).addProduct(anyString(), any(AddNewProductRequest.class));

        mockMvc.perform(
                patch(BASE_URI + "/" + WISHLIST_ID + "/add-product")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("When call addProduct method, with valid wishlistId but invalid product id, then return business exception")
    void whenCall_addProductMethodWithValidWishlistIdButInvalidProductId_thenReturnBusinessException() throws Exception {
        AddNewProductRequest request = new AddNewProductRequest("123", 1);
        doNothing().when(wishlistService).addProduct(anyString(), any(AddNewProductRequest.class));

        mockMvc.perform(
                patch(BASE_URI + "/" + WISHLIST_ID + "/add-product")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error on payload validation attributes"))
                .andExpect(jsonPath("$.error").value("Validation Exception"))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/" + WISHLIST_ID + "/add-product"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='id' && @.message=='Field id must be between 14 and 30 characters')]").exists());
    }

    @Test
    @DisplayName("When call findProductById method, with valid wishlistId and valid productId, then return product response")
    void whenCall_findProductByIdMethodWithValidWishlistIdAndValidProductId_thenReturnProductResponse() throws Exception {
        WishlistResponse response = generateMock(WishlistResponse.class);
        when(wishlistService.findProductById(anyString(), anyString())).thenReturn(response.products().iterator().next());

        mockMvc.perform(
                get(BASE_URI + "/" + WISHLIST_ID + "/products/123")
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.products().iterator().next().getId()))
                .andExpect(jsonPath("$.name").value(response.products().iterator().next().getName()))
                .andExpect(jsonPath("$.unityPrice").value(response.products().iterator().next().getUnityPrice()))
                .andExpect(jsonPath("$.quantity").value(response.products().iterator().next().getQuantity()));
    }

    @Test
    @DisplayName("When call removeProduct method, with valid wishlistId and valid productId, then return no content status")
    void whenCall_removeProductMethodWithValidWishlistIdAndValidProductId_thenReturnNoContentStatus() throws Exception {
        doNothing().when(wishlistService).removeProduct(anyString(), anyString());

        mockMvc.perform(
                delete(BASE_URI + "/" + WISHLIST_ID + "/products/123")
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    private String toJson(final Object object) throws Exception {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (final Exception e) {
            throw new Exception("Error to convert object to json", e);
        }
    }
}