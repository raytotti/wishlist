package com.raytotti.wishlist.domain;

import com.raytotti.wishlist.application.WishlistAddProductRequest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleProductTest {

    private final String PRODUCT_ID = ObjectId.get().toHexString();
    private final String CODE = "PRODUCT-CODE";
    private final String DESCRIPTION = "Product Description";
    private final String THUMBNAIL = "Image URL";
    private final BigDecimal PRICE = BigDecimal.TEN;

    @Test
    void of() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        SimpleProduct simpleProduct = SimpleProduct.of(request);

        assertEquals(PRODUCT_ID, simpleProduct.getId());
        assertEquals(CODE, simpleProduct.getCode());
        assertEquals(DESCRIPTION, simpleProduct.getDescription());
        assertEquals(THUMBNAIL, simpleProduct.getThumbnail());
        assertEquals(PRICE, simpleProduct.getPrice());
    }

    @Test
    void of_with_null() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> SimpleProduct.of(null));
    }

    @Test
    void of_with_product_id_null() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                null,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);

        assertThrows(NullPointerException.class, () -> SimpleProduct.of(request));
    }

    @Test
    void of_with_code_null() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                null,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);

        assertThrows(NullPointerException.class, () -> SimpleProduct.of(request));
    }

    @Test
    void of_with_description_null() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                null,
                THUMBNAIL,
                PRICE);

        assertThrows(NullPointerException.class, () -> SimpleProduct.of(request));
    }

    @Test
    void of_with_thumbnail_null() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                null,
                PRICE);

        assertThrows(NullPointerException.class, () -> SimpleProduct.of(request));
    }

    @Test
    void of_with_price_null() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                null);

        assertThrows(NullPointerException.class, () -> SimpleProduct.of(request));
    }


    @Test
    void test_equals() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        SimpleProduct simpleProduct = SimpleProduct.of(request);

        SimpleProduct simpleProductEqual = SimpleProduct.of(request);

        assertEquals(simpleProduct, simpleProductEqual);
    }

    @Test
    void test_not_equals() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        SimpleProduct simpleProduct = SimpleProduct.of(request);

        WishlistAddProductRequest requestNotEquals = new WishlistAddProductRequest(
                ObjectId.get().toHexString(),
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        SimpleProduct simpleProductNotEquals = SimpleProduct.of(requestNotEquals);

        assertNotEquals(simpleProduct, simpleProductNotEquals);
    }

    @Test
    void test_not_equals_with_null() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        SimpleProduct simpleProduct = SimpleProduct.of(request);

        assertNotEquals(simpleProduct, null);
    }

    @Test
    void test_not_equals_with_other_object() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        SimpleProduct simpleProduct = SimpleProduct.of(request);

        assertNotEquals(simpleProduct, new Object());
    }

    @Test
    void test_hashCode() {
        WishlistAddProductRequest request = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        SimpleProduct simpleProduct = SimpleProduct.of(request);

        assertEquals(Objects.hash(PRODUCT_ID), simpleProduct.hashCode());
    }
}