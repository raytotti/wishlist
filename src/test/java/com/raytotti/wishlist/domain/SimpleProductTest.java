package com.raytotti.wishlist.domain;

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
        SimpleProduct simpleProduct = SimpleProduct.of(PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE
        );

        assertEquals(PRODUCT_ID, simpleProduct.getId());
        assertEquals(CODE, simpleProduct.getCode());
        assertEquals(DESCRIPTION, simpleProduct.getDescription());
        assertEquals(THUMBNAIL, simpleProduct.getThumbnail());
        assertEquals(PRICE, simpleProduct.getPrice());
    }

    @Test
    void of_with_null() {
        assertThrows(NullPointerException.class, () -> SimpleProduct.of(null, null, null, null, null));
    }

    @Test
    void of_with_product_id_null() {
        assertThrows(NullPointerException.class, () -> SimpleProduct.of(null,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE));
    }

    @Test
    void of_with_code_null() {
        assertThrows(NullPointerException.class, () -> SimpleProduct.of(PRODUCT_ID,
                null,
                DESCRIPTION,
                THUMBNAIL,
                PRICE));
    }

    @Test
    void of_with_description_null() {
        assertThrows(NullPointerException.class, () -> SimpleProduct.of(PRODUCT_ID,
                CODE,
                null,
                THUMBNAIL,
                PRICE));
    }

    @Test
    void of_with_thumbnail_null() {
        assertThrows(NullPointerException.class, () -> SimpleProduct.of(PRODUCT_ID,
                CODE,
                DESCRIPTION,
                null,
                PRICE));
    }

    @Test
    void of_with_price_null() {
        assertThrows(NullPointerException.class, () -> SimpleProduct.of(PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                null));
    }


    @Test
    void test_equals() {
        SimpleProduct simpleProduct = SimpleProduct.of(PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);

        SimpleProduct simpleProductEqual = SimpleProduct.of(PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);

        assertEquals(simpleProduct, simpleProductEqual);
    }

    @Test
    void test_not_equals() {
        SimpleProduct simpleProduct = SimpleProduct.of(PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);

        SimpleProduct simpleProductNotEquals = SimpleProduct.of(ObjectId.get().toHexString(),
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);

        assertNotEquals(simpleProduct, simpleProductNotEquals);
    }

    @Test
    void test_not_equals_with_null() {
        SimpleProduct simpleProduct = SimpleProduct.of(PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);

        assertNotEquals(simpleProduct, null);
    }

    @Test
    void test_not_equals_with_other_object() {
        SimpleProduct simpleProduct = SimpleProduct.of(PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);

        assertNotEquals(simpleProduct, new Object());
    }

    @Test
    void test_hashCode() {
        SimpleProduct simpleProduct = SimpleProduct.of(PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);

        assertEquals(Objects.hash(PRODUCT_ID), simpleProduct.hashCode());
    }
}