package br.com.valdircezar.wishlist.controllers;

import br.com.valdircezar.wishlist.models.entities.Wishlist;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
import br.com.valdircezar.wishlist.models.responses.WishlistResponse;
import br.com.valdircezar.wishlist.services.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static br.com.valdircezar.wishlist.stub.WishlistStub.generateMock;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    /* -------------------- TESTES PARA O ENDPOINT POST /v1/wishlists -------------------- */
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
        Mockito.when(wishlistService.save(Mockito.any(CreateWishlistRequest.class))).thenReturn(entity);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(header().string("location", "http://localhost/v1/wishlists/" + entity.getId()));
    }

    /* -------------------- TESTES PARA O ENDPOINT GET /v1/wishlists/{wishlistId} -------------------- */
    @Test
    @DisplayName("When call findById method, with valid id, then return wishlist response")
    void whenCall_findByIdMethodWithValidId_thenReturnWishlistResponse() throws Exception {
        WishlistResponse response = generateMock(WishlistResponse.class);
        Mockito.when(wishlistService.findById(anyString())).thenReturn(response);

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

    private String toJson(final Object object) throws Exception {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (final Exception e) {
            throw new Exception("Error to convert object to json", e);
        }
    }
}