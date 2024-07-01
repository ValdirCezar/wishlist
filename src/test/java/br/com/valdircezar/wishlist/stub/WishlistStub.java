package br.com.valdircezar.wishlist.stub;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class WishlistStub {
    private static final PodamFactory factory = new PodamFactoryImpl();

    public static <T> T generateMock(final Class<T> clazz) {
        return factory.manufacturePojo(clazz);
    }
}
