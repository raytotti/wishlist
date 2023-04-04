package com.raytotti.wishlist.application;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WishlistAddProductRequestTest {

    private final String PRODUCT_ID_NOT_NULL = "{Product.productId.NotNull}";
    private final String PRODUCT_ID = ObjectId.get().toHexString();
    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void build() {
        WishlistAddProductRequest wishlistAddProductRequest = new WishlistAddProductRequest(PRODUCT_ID);
        Set<ConstraintViolation<WishlistAddProductRequest>> violations = validator.validate(wishlistAddProductRequest);

        assertEquals(PRODUCT_ID, wishlistAddProductRequest.getProductId());
        assertTrue(violations.isEmpty());
    }

    @Test
    void build_with_productId_null() {
        WishlistAddProductRequest wishlistAddProductRequest = new WishlistAddProductRequest(null);
        Set<ConstraintViolation<WishlistAddProductRequest>> violations = validator.validate(wishlistAddProductRequest);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessageTemplate().equals(PRODUCT_ID_NOT_NULL)));
    }
}
