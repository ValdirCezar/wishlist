package br.com.valdircezar.wishlist.mappers;

import br.com.valdircezar.wishlist.models.entities.Wishlist;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;
import br.com.valdircezar.wishlist.models.responses.WishlistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS
)
public interface WishlistMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Wishlist toEntity(CreateWishlistRequest request);

    WishlistResponse toResponse(Wishlist entity, BigDecimal totalValue);
}
