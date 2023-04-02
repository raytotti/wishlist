package com.raytotti.wishlist.application;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WishlistAddProductRequest {

    @NotNull(message = "{Product.productId.NotNull}")
    private String productId;

    @NotNull(message = "{Product.code.NotNull}")
    private String code;

    @NotNull(message = "{Product.description.NotNull}")
    private String description;

    @NotNull(message = "{Product.thumbnail.NotNull}")
    private String thumbnail;

    @NotNull(message = "{Product.price.NotNull}")
    private BigDecimal price;

}
