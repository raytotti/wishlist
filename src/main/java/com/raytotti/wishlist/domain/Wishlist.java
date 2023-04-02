package com.raytotti.wishlist.domain;

import com.raytotti.wishlist.application.WishlistAddProductRequest;
import com.raytotti.wishlist.exception.MaxLimitProductException;
import com.raytotti.wishlist.exception.ProductExistsException;
import com.raytotti.wishlist.exception.ProductNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Document
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Wishlist {

    @Id
    private String id;
    @Indexed(unique = true)
    private ObjectId clientId;
    private Set<SimpleProduct> products = new LinkedHashSet<>();

    public void addProduct(@NotNull final WishlistAddProductRequest request) {
        if (this.products.size() == 20) {
            throw new MaxLimitProductException();
        }
        if (this.products.stream().anyMatch(product -> product.getId().equals(request.getProductId()))) {
            throw new ProductExistsException();
        }
        this.products.add(SimpleProduct.of(request));
    }

    public void removeProduct(@NotNull final String productId) {
        if (!this.products.removeIf(product -> product.getId().equals(productId))) {
            throw new ProductNotFoundException();
        }
    }

    public static Wishlist of(@NotNull final String clientId, @NotNull final WishlistAddProductRequest request) {
        return new Wishlist(
                null,
                new ObjectId(clientId),
                new LinkedHashSet<>(Collections.singleton(SimpleProduct.of(request)))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wishlist wishlist = (Wishlist) o;
        return clientId.equals(wishlist.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }
}
