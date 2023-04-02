package com.raytotti.wishlist.application;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WishlistAddProductRequestTest {

    private final String PRODUCT_ID_NOT_NULL = "{Product.productId.NotNull}";
    private final String CODE_NOT_NULL = "{Product.code.NotNull}";
    private final String DESCRIPTION_NOT_NULL = "{Product.description.NotNull}";
    private final String THUMBNAIL_NOT_NULL = "{Product.thumbnail.NotNull}";
    private final String PRICE_NOT_NULL = "{Product.price.NotNull}";
    private final String PRODUCT_ID = ObjectId.get().toHexString();
    private final String CODE = "PRODUCT-CODE";
    private final String DESCRIPTION = "Product Description";
    private final String THUMBNAIL = "Image URL";
    private final BigDecimal PRICE = BigDecimal.TEN;

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void build() {
        WishlistAddProductRequest wishlistAddProductRequest = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        Set<ConstraintViolation<WishlistAddProductRequest>> violations = validator.validate(wishlistAddProductRequest);

        assertEquals(PRODUCT_ID, wishlistAddProductRequest.getProductId());
        assertEquals(CODE, wishlistAddProductRequest.getCode());
        assertEquals(DESCRIPTION, wishlistAddProductRequest.getDescription());
        assertEquals(THUMBNAIL, wishlistAddProductRequest.getThumbnail());
        assertEquals(PRICE, wishlistAddProductRequest.getPrice());
        assertTrue(violations.isEmpty());
    }

    @Test
    void build_with_productId_null() {
        WishlistAddProductRequest wishlistAddProductRequest = new WishlistAddProductRequest(
                null,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        Set<ConstraintViolation<WishlistAddProductRequest>> violations = validator.validate(wishlistAddProductRequest);

        assertEquals(CODE, wishlistAddProductRequest.getCode());
        assertEquals(DESCRIPTION, wishlistAddProductRequest.getDescription());
        assertEquals(THUMBNAIL, wishlistAddProductRequest.getThumbnail());
        assertEquals(PRICE, wishlistAddProductRequest.getPrice());
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(PRODUCT_ID_NOT_NULL)));
    }

    @Test
    void build_with_code_null() {
        WishlistAddProductRequest wishlistAddProductRequest = new WishlistAddProductRequest(
                PRODUCT_ID,
                null,
                DESCRIPTION,
                THUMBNAIL,
                PRICE);
        Set<ConstraintViolation<WishlistAddProductRequest>> violations = validator.validate(wishlistAddProductRequest);

        assertEquals(PRODUCT_ID, wishlistAddProductRequest.getProductId());
        assertEquals(DESCRIPTION, wishlistAddProductRequest.getDescription());
        assertEquals(THUMBNAIL, wishlistAddProductRequest.getThumbnail());
        assertEquals(PRICE, wishlistAddProductRequest.getPrice());
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(CODE_NOT_NULL)));
    }

    @Test
    void build_with_description_null() {
        WishlistAddProductRequest wishlistAddProductRequest = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                null,
                THUMBNAIL,
                PRICE);
        Set<ConstraintViolation<WishlistAddProductRequest>> violations = validator.validate(wishlistAddProductRequest);

        assertEquals(PRODUCT_ID, wishlistAddProductRequest.getProductId());
        assertEquals(CODE, wishlistAddProductRequest.getCode());
        assertEquals(THUMBNAIL, wishlistAddProductRequest.getThumbnail());
        assertEquals(PRICE, wishlistAddProductRequest.getPrice());
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(DESCRIPTION_NOT_NULL)));
    }

    @Test
    void build_with_thumbnail_null() {
        WishlistAddProductRequest wishlistAddProductRequest = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                null,
                PRICE);
        Set<ConstraintViolation<WishlistAddProductRequest>> violations = validator.validate(wishlistAddProductRequest);

        assertEquals(PRODUCT_ID, wishlistAddProductRequest.getProductId());
        assertEquals(CODE, wishlistAddProductRequest.getCode());
        assertEquals(DESCRIPTION, wishlistAddProductRequest.getDescription());
        assertEquals(PRICE, wishlistAddProductRequest.getPrice());
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(THUMBNAIL_NOT_NULL)));
    }

    @Test
    void build_with_price_null() {
        WishlistAddProductRequest wishlistAddProductRequest = new WishlistAddProductRequest(
                PRODUCT_ID,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                null);
        Set<ConstraintViolation<WishlistAddProductRequest>> violations = validator.validate(wishlistAddProductRequest);

        assertEquals(PRODUCT_ID, wishlistAddProductRequest.getProductId());
        assertEquals(CODE, wishlistAddProductRequest.getCode());
        assertEquals(DESCRIPTION, wishlistAddProductRequest.getDescription());
        assertEquals(THUMBNAIL, wishlistAddProductRequest.getThumbnail());
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(PRICE_NOT_NULL)));
    }

    @Test
    void build_with_all_null() {
        WishlistAddProductRequest wishlistAddProductRequest = new WishlistAddProductRequest(
                null,
                null,
                null,
                null,
                null);
        Set<ConstraintViolation<WishlistAddProductRequest>> violations = validator.validate(wishlistAddProductRequest);

        assertFalse(violations.isEmpty());
        assertEquals(5, violations.size());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(PRODUCT_ID_NOT_NULL)));
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(CODE_NOT_NULL)));
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(DESCRIPTION_NOT_NULL)));
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(THUMBNAIL_NOT_NULL)));
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(PRICE_NOT_NULL)));
    }
}
