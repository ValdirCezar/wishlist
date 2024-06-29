package br.com.valdircezar.wishlist.clients;

import br.com.valdircezar.wishlist.models.exceptions.ObjectNotFoundException;
import br.com.valdircezar.wishlist.models.responses.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public class ProductClientMock {

    public static ProductResponse findById(final String productId) {
        return mockProductResponse()
                .stream().filter(product -> product.id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Product not found by id: " + productId));
    }

    private static List<ProductResponse> mockProductResponse() {
        return List.of(
                // Need 21 products
                new ProductResponse("336b3efb74e106091aae50d3", "HyperX Headset", BigDecimal.valueOf(800.0)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae4", "Apple Mouse", BigDecimal.valueOf(200.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4becag", "iPhone Case", BigDecimal.valueOf(100.0)),
                new ProductResponse("333abc510d2ef9143eefa7f1", "Apple Watch", BigDecimal.valueOf(600.0)),
                new ProductResponse("336b3efb74e106091aae50d8", "MacBook Case", BigDecimal.valueOf(200.0)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae9", "Apple Keyboard", BigDecimal.valueOf(300.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4becah", "Apple AirPods", BigDecimal.valueOf(400.0)),
                new ProductResponse("333abc510d2ef9143eefa7f2", "Apple Pencil", BigDecimal.valueOf(150.0)),
                new ProductResponse("336b3efb74e106091aae50d9", "Apple Charger", BigDecimal.valueOf(100.0)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae1", "Apple Monitor", BigDecimal.valueOf(1000.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4beca1", "Apple TV", BigDecimal.valueOf(500.0)),
                new ProductResponse("333abc510d2ef9143eefa7f3", "Apple HomePod", BigDecimal.valueOf(300.0)),
                new ProductResponse("336b3efb74e106091aae50d1", "Apple Magic Trackpad", BigDecimal.valueOf(200.0)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae2", "Apple Magic Mouse", BigDecimal.valueOf(150.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4beca2", "Apple Magic Keyboard", BigDecimal.valueOf(200.0)),
                new ProductResponse("333abc510d2ef9143eefa7f4", "Apple Magic Trackpad 2", BigDecimal.valueOf(250.0)),
                new ProductResponse("336b3efb74e106091aae50d2", "Apple Magic Mouse 2", BigDecimal.valueOf(200.0)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae3", "Apple Magic Keyboard 2", BigDecimal.valueOf(250.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4beca3", "Apple Magic Trackpad 3", BigDecimal.valueOf(300.0)),
                new ProductResponse("333abc510d2ef9143eefa7f5", "Apple Magic Mouse 3", BigDecimal.valueOf(250.0)),
                new ProductResponse("336b3efb74e106091aae50d4", "Apple Magic Keyboard 3", BigDecimal.valueOf(300.0))
        );
    }

}
