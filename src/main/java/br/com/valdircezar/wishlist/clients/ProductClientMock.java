package br.com.valdircezar.wishlist.clients;

import br.com.valdircezar.wishlist.models.exceptions.ObjectNotFoundException;
import br.com.valdircezar.wishlist.models.responses.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public class ProductClientMock {

    public static ProductResponse findById(final String productId) {
        return getProductMockResponse()
                .stream().filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Product not found by id: " + productId));
    }

    public static List<ProductResponse> getProductMockResponse() {
        return List.of(
                new ProductResponse("336b3efb74e106091aae50d3", "HyperX Headset", null, BigDecimal.valueOf(800.50)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae4", "Apple Mouse", null, BigDecimal.valueOf(200.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4becag", "iPhone Case", null, BigDecimal.valueOf(100.50)),
                new ProductResponse("333abc510d2ef9143eefa7f1", "Apple Watch", null, BigDecimal.valueOf(600.0)),
                new ProductResponse("336b3efb74e106091aae50d8", "MacBook Case", null, BigDecimal.valueOf(200.50)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae9", "Apple Keyboard", null, BigDecimal.valueOf(300.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4becah", "Apple AirPods", null, BigDecimal.valueOf(400.50)),
                new ProductResponse("333abc510d2ef9143eefa7f2", "Apple Pencil", null, BigDecimal.valueOf(150.0)),
                new ProductResponse("336b3efb74e106091aae50d9", "Apple Charger", null, BigDecimal.valueOf(100.50)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae1", "Apple Monitor", null, BigDecimal.valueOf(1000.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4beca1", "Apple TV", null, BigDecimal.valueOf(500.50)),
                new ProductResponse("333abc510d2ef9143eefa7f3", "Apple HomePod", null, BigDecimal.valueOf(300.0)),
                new ProductResponse("336b3efb74e106091aae50d1", "Apple Magic Trackpad", null, BigDecimal.valueOf(200.50)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae2", "Apple Magic Mouse", null, BigDecimal.valueOf(150.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4beca2", "Apple Magic Keyboard", null, BigDecimal.valueOf(200.50)),
                new ProductResponse("333abc510d2ef9143eefa7f4", "Apple Magic Trackpad 2", null, BigDecimal.valueOf(250.0)),
                new ProductResponse("336b3efb74e106091aae50d2", "Apple Magic Mouse 2", null, BigDecimal.valueOf(200.50)),
                new ProductResponse("997f2e1a9f5cf6e2ca4beae3", "Apple Magic Keyboard 2", null, BigDecimal.valueOf(250.0)),
                new ProductResponse("693f2e1a9f5cf6e2ca4beca3", "Apple Magic Trackpad 3", null, BigDecimal.valueOf(300.50)),
                new ProductResponse("333abc510d2ef9143eefa7f5", "Apple Magic Mouse 3", null, BigDecimal.valueOf(250.0)),
                new ProductResponse("336b3efb74e106091aae50d4", "Apple Magic Keyboard 3", null, BigDecimal.valueOf(300.50))
        );
    }

}
