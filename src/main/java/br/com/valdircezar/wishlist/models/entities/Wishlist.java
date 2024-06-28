package br.com.valdircezar.wishlist.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wishlist")
public class Wishlist {

    @Id
    private String id;
    private String name;
    private String userId;

    @Builder.Default
    private Set<String> productsId = Set.of();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
