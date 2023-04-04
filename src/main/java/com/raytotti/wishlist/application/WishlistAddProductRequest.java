package com.raytotti.wishlist.application;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WishlistAddProductRequest {

    @NotBlank(message = "{Product.productId.NotNull}")
    private String productId;

}
