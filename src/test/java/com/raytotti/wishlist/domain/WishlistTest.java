package com.raytotti.wishlist.domain;

import com.raytotti.wishlist.application.WishlistAddProductRequest;
import com.raytotti.wishlist.exception.MaxLimitProductException;
import com.raytotti.wishlist.exception.ProductExistsException;
import com.raytotti.wishlist.exception.ProductNotFoundException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class WishlistTest {

    private final ObjectId CLIENT_ID = ObjectId.get();
    private final String PRODUCT_ID = ObjectId.get().toHexString();
    private final String CODE = "PRODUCT-CODE";
    private final String DESCRIPTION = "Product Description";
    private final String THUMBNAIL = "Image URL";
    private final BigDecimal PRICE = BigDecimal.TEN;

    private final WishlistAddProductRequest REQUEST = new WishlistAddProductRequest(
            PRODUCT_ID,
            CODE,
            DESCRIPTION,
            THUMBNAIL,
            PRICE
    );


    @Test
    void addProduct() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);
        String newProductId = ObjectId.get().toHexString();
        WishlistAddProductRequest otherProduct = new WishlistAddProductRequest(
                newProductId,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE
        );

        wishlist.addProduct(otherProduct);

        assertEquals(2, wishlist.getProducts().size());
        assertTrue(wishlist.getProducts().stream().anyMatch(product -> product.getId().equals(newProductId)));
    }

    @Test
    void addProduct_with_null() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);
        assertThrows(NullPointerException.class, () -> wishlist.addProduct(null));
    }

    @Test
    void addProduct_max_limit_20() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);
        for (int i = 1; i < 20; i++) {
            WishlistAddProductRequest otherProduct = new WishlistAddProductRequest(
                    ObjectId.get().toHexString(),
                    CODE,
                    DESCRIPTION,
                    THUMBNAIL,
                    PRICE
            );
            wishlist.addProduct(otherProduct);
        }

        assertEquals(20, wishlist.getProducts().size());

        WishlistAddProductRequest otherProduct = new WishlistAddProductRequest(
                ObjectId.get().toHexString(),
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE
        );
        assertThrows(MaxLimitProductException.class, () -> wishlist.addProduct(otherProduct));
    }

    @Test
    void addProduct_exists() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);
        assertThrows(ProductExistsException.class, () -> wishlist.addProduct(REQUEST));
    }

    @Test
    void removeProduct() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);

        wishlist.removeProduct(REQUEST.getProductId());

        assertEquals(0, wishlist.getProducts().size());
    }

    @Test
    void remove_one_product() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);

        for (int i = 1; i < 20; i++) {
            WishlistAddProductRequest otherProduct = new WishlistAddProductRequest(
                    ObjectId.get().toHexString(),
                    CODE,
                    DESCRIPTION,
                    THUMBNAIL,
                    PRICE
            );
            wishlist.addProduct(otherProduct);
        }

        assertEquals(20, wishlist.getProducts().size());

        wishlist.removeProduct(REQUEST.getProductId());

        assertEquals(19, wishlist.getProducts().size());
    }

    @Test
    void removeProduct_not_found() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);

        assertThrows(ProductNotFoundException.class, () -> wishlist.removeProduct(ObjectId.get().toHexString()));
    }

    @Test
    void of() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);

        assertNull(wishlist.getId());
        assertEquals(CLIENT_ID, wishlist.getClientId());
        assertEquals(1, wishlist.getProducts().size());
        SimpleProduct simpleProduct = wishlist.getProducts().stream().findFirst().orElseThrow();
        assertEquals(PRODUCT_ID, simpleProduct.getId());
        assertEquals(CODE, simpleProduct.getCode());
        assertEquals(DESCRIPTION, simpleProduct.getDescription());
        assertEquals(THUMBNAIL, simpleProduct.getThumbnail());
        assertEquals(PRICE, simpleProduct.getPrice());
    }

    @Test
    void of_clientId_null() {
        assertThrows(IllegalArgumentException.class, () -> Wishlist.of(null, REQUEST));
    }

    @Test
    void of_request_null() {
        assertThrows(NullPointerException.class, () -> Wishlist.of(CLIENT_ID.toHexString(), null));
    }

    @Test
    void of_clientId_any() {
        assertThrows(IllegalArgumentException.class, () -> Wishlist.of("any string", REQUEST));
    }

    @Test
    void test_equals() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);
        Wishlist wishlistEquals = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);

        assertEquals(wishlist, wishlistEquals);
    }

    @Test
    void test_not_equals() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);
        Wishlist wishlistNotEquals = Wishlist.of(ObjectId.get().toHexString(), REQUEST);

        assertNotEquals(wishlist, wishlistNotEquals);
    }

    @Test
    void test_not_equals_with_null() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);
        assertNotEquals(wishlist, null);
    }

    @Test
    void test_not_equals_with_other_object() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);
        assertNotEquals(wishlist, new Object());
    }

    @Test
    void test_hashCode() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), REQUEST);
        assertEquals(Objects.hash(CLIENT_ID), wishlist.hashCode());
    }
}