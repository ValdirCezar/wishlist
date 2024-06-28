package br.com.valdircezar.wishlist.clients;

import br.com.valdircezar.wishlist.models.exceptions.ObjectNotFoundException;
import br.com.valdircezar.wishlist.models.responses.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public class ProductClientMock {

    public static void findById(final String productId) {
        mockProductResponse()
                .stream().filter(product -> product.id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Product not found by id: " + productId));
    }

    private static List<ProductResponse> mockProductResponse() {
        return List.of(
                new ProductResponse("336b3efb74e106091aae50d3", "HyperX Headset", BigDecimal.valueOf(800.0)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae4", "Apple Mouse", BigDecimal.valueOf(200.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4becag", "iPhone Case", BigDecimal.valueOf(100.0)),
                new ProductResponse("333abc510d2ef9143eefa7f1", "Apple Watch", BigDecimal.valueOf(600.0)),
                new ProductResponse("336b3efb74e106091aae50d8", "MacBook Case", BigDecimal.valueOf(200.0))
        );
    }

}
