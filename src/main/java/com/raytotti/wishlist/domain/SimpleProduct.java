package com.raytotti.wishlist.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleProduct {

    @Id
    private String id;
    private String code;
    private String description;
    private String thumbnail;
    private BigDecimal price;

    public static SimpleProduct of(
            @NotNull final String id,
            @NotNull final String code,
            @NotNull final String description,
            @NotNull final String thumbnail,
            @NotNull final BigDecimal price) {
        return new SimpleProduct(
                Objects.requireNonNull(id),
                Objects.requireNonNull(code),
                Objects.requireNonNull(description),
                Objects.requireNonNull(thumbnail),
                Objects.requireNonNull(price));
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
