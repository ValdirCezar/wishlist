package br.com.valdircezar.wishlist.repositories;

import br.com.valdircezar.wishlist.models.entities.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {
}
