package br.com.valdircezar.wishlist.models.responses;

import java.math.BigDecimal;

public record ProductResponse(String id, String name, BigDecimal price) {
}
