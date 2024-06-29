package br.com.valdircezar.wishlist.services.validations;

import br.com.valdircezar.wishlist.clients.ProductClientMock;
import br.com.valdircezar.wishlist.clients.UserClientMock;
import br.com.valdircezar.wishlist.models.exceptions.BusinessException;
import br.com.valdircezar.wishlist.models.requests.CreateWishlistRequest;

/**
 * Como os clients para a validação do usuário e produto são mocks
 * essa classe não foi criada como um componente do spring para que
 * pudesse ser injetada no serviço de wishlist. Em um cenário real
 * essa classe seria um componente do spring e seria injetada no serviço
 * com as devidas dependências e validações.
 */
public class WishlistPreValidation {

    /**
     * This method applies the validation rules for the creation of a wishlist.
     *
     * @param wishlistRequest the request to create a wishlist
     */
    public static void applyValidationRules(CreateWishlistRequest wishlistRequest) {
        validateUserExists(wishlistRequest);
        validateProductExists(wishlistRequest);
        validateProductsQuantity(wishlistRequest.productsId().size());
    }

    private static void validateProductsQuantity(int productsQuantity) {
        if (productsQuantity > 20) throw new BusinessException("Wishlist cannot have more than 20 products");
    }

    private static void validateUserExists(CreateWishlistRequest wishlistRequest) {
        UserClientMock.findById(wishlistRequest.userId());
    }

    private static void validateProductExists(CreateWishlistRequest wishlistRequest) {
        wishlistRequest.productsId().forEach(ProductClientMock::findById);
    }
}
