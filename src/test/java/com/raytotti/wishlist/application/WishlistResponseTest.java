package com.raytotti.wishlist.application;

import com.raytotti.wishlist.domain.SimpleProduct;
import com.raytotti.wishlist.domain.Wishlist;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WishlistResponseTest {

    @Test
    void from() {
        String clientId = ObjectId.get().toHexString();
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                ObjectId.get().toHexString(),
                "PRODUCT-CODE",
                "Product Description",
                "Image URL",
                BigDecimal.TEN
        );
        Wishlist wishlist = Wishlist.of(clientId, request);

        WishlistResponse response = WishlistResponse.from(wishlist);

        assertEquals(wishlist.getId(), response.getId());
        assertEquals(wishlist.getClientId().toHexString(), response.getClientId());
        assertEquals(wishlist.getProducts().size(), response.getProducts().size());

        SimpleProduct wishListProduct = wishlist.getProducts().stream().findFirst().orElseThrow();
        SimpleProduct wishListResponse = response.getProducts().stream().findFirst().orElseThrow();
        assertEquals(wishListProduct.getId(), wishListResponse.getId());
        assertEquals(wishListProduct.getCode(), wishListResponse.getCode());
        assertEquals(wishListProduct.getDescription(), wishListResponse.getDescription());
        assertEquals(wishListProduct.getThumbnail(), wishListResponse.getThumbnail());
        assertEquals(wishListProduct.getPrice(), wishListResponse.getPrice());
    }

    @Test
    void from_with_wishlist_null() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> WishlistResponse.from(null));
    }
}