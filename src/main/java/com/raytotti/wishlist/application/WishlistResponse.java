package com.raytotti.wishlist.application;

import com.raytotti.wishlist.domain.SimpleProduct;
import com.raytotti.wishlist.domain.Wishlist;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WishlistResponse {

    private String id;
    private String clientId;
    private Set<SimpleProduct> products;

    public static WishlistResponse from(@NotNull final Wishlist wishlist) {
        return new WishlistResponse(wishlist.getId(), wishlist.getClientId().toString(), wishlist.getProducts());
    }
}
