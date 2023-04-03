package com.raytotti.wishlist.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WishlistAddProductRequest {

    @NotBlank(message = "{Product.productId.NotNull}")
    private String productId;

    @NotBlank(message = "{Product.code.NotNull}")
    private String code;

    @NotBlank(message = "{Product.description.NotNull}")
    private String description;

    @NotBlank(message = "{Product.thumbnail.NotNull}")
    private String thumbnail;

    @Positive(message = "{Product.price.Positive}")
    @NotNull(message = "{Product.price.NotNull}")
    private BigDecimal price;

}
