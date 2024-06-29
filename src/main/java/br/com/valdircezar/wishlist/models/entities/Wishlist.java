package br.com.valdircezar.wishlist.models.entities;

import br.com.valdircezar.wishlist.models.requests.ProductRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wishlist")
public class Wishlist implements Serializable {

    @Serial
    private static final long serialVersionUID = 3940333998951779679L;

    @Id
    private String id;
    private String name;
    private String userId;

    @Builder.Default
    private Set<ProductRequest> products = Set.of();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
