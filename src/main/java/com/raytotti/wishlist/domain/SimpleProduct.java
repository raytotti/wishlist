package com.raytotti.wishlist.domain;

import com.raytotti.wishlist.application.WishlistAddProductRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleProduct {

    @Id
    private String id;
    private String code;
    private String description;
    private String thumbnail;
    private BigDecimal price;

    public static SimpleProduct of(@NotNull final WishlistAddProductRequest request) {
        return new SimpleProduct(
                Objects.requireNonNull(request.getProductId()),
                Objects.requireNonNull(request.getCode()),
                Objects.requireNonNull(request.getDescription()),
                Objects.requireNonNull(request.getThumbnail()),
                Objects.requireNonNull(request.getPrice())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleProduct product = (SimpleProduct) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
