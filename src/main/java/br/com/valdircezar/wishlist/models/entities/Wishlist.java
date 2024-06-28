package br.com.valdircezar.wishlist.models.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "wishlist")
public class Wishlist {

    @Id
    private String id;
    private String name;
    private String userId;
    private List<String> productsId;
    private String createdAt;

}
