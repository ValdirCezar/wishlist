package br.com.valdircezar.wishlist.configs;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OpenAPIConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void customOpenAPI_ShouldReturnCorrectlyConfiguredOpenAPI() {
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Wishlist API");
        assertThat(openAPI.getInfo().getDescription()).isEqualTo("API exemplo para gerenciamento de lista de desejos em um e-commerce.");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
    }
}