package com.raytotti.wishlist.domain;

import com.raytotti.wishlist.exception.MaxLimitProductException;
import com.raytotti.wishlist.exception.ProductExistsException;
import com.raytotti.wishlist.exception.ProductNotFoundException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WishlistTest {

    private final ObjectId CLIENT_ID = ObjectId.get();
    private final String PRODUCT_ID = ObjectId.get().toHexString();
    private final String CODE = "PRODUCT-CODE";
    private final String DESCRIPTION = "Product Description";
    private final String THUMBNAIL = "Image URL";
    private final BigDecimal PRICE = BigDecimal.TEN;

    private final SimpleProduct PRODUCT = SimpleProduct.of(
            PRODUCT_ID,
            CODE,
            DESCRIPTION,
            THUMBNAIL,
            PRICE
    );

    @Test
    public void test_constructor_is_private() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<Wishlist> constructor = Wishlist.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    void addProduct() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);
        String newProductId = ObjectId.get().toHexString();
        SimpleProduct otherProduct = SimpleProduct.of(
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
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);
        assertThrows(NullPointerException.class, () -> wishlist.addProduct(null));
    }

    @Test
    void addProduct_max_limit_20() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);
        for (int i = 1; i < 20; i++) {
            SimpleProduct otherProduct = SimpleProduct.of(
                    ObjectId.get().toHexString(),
                    CODE,
                    DESCRIPTION,
                    THUMBNAIL,
                    PRICE
            );
            wishlist.addProduct(otherProduct);
        }

        assertEquals(20, wishlist.getProducts().size());

        SimpleProduct otherProduct = SimpleProduct.of(
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
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);
        assertThrows(ProductExistsException.class, () -> wishlist.addProduct(PRODUCT));
    }

    @Test
    void removeProduct() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);

        wishlist.removeProduct(PRODUCT.getId());

        assertEquals(0, wishlist.getProducts().size());
    }

    @Test
    void remove_one_product() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);

        for (int i = 1; i < 20; i++) {
            SimpleProduct otherProduct = SimpleProduct.of(
                    ObjectId.get().toHexString(),
                    CODE,
                    DESCRIPTION,
                    THUMBNAIL,
                    PRICE
            );
            wishlist.addProduct(otherProduct);
        }

        assertEquals(20, wishlist.getProducts().size());

        wishlist.removeProduct(PRODUCT.getId());

        assertEquals(19, wishlist.getProducts().size());
    }

    @Test
    void removeProduct_not_found() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);

        assertThrows(ProductNotFoundException.class, () -> wishlist.removeProduct(ObjectId.get().toHexString()));
    }

    @Test
    void of() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);

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
        assertThrows(IllegalArgumentException.class, () -> Wishlist.of(null, PRODUCT));
    }

    @Test
    void of_request_null() {
        assertThrows(NullPointerException.class, () -> Wishlist.of(CLIENT_ID.toHexString(), null));
    }

    @Test
    void of_clientId_any() {
        assertThrows(IllegalArgumentException.class, () -> Wishlist.of("any string", PRODUCT));
    }

    @Test
    void test_equals() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);
        Wishlist wishlistEquals = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);

        assertEquals(wishlist, wishlistEquals);
    }

    @Test
    void test_not_equals() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);
        Wishlist wishlistNotEquals = Wishlist.of(ObjectId.get().toHexString(), PRODUCT);

        assertNotEquals(wishlist, wishlistNotEquals);
    }

    @Test
    void test_not_equals_with_null() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);
        assertNotEquals(wishlist, null);
    }

    @Test
    void test_not_equals_with_other_object() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);
        assertNotEquals(wishlist, new Object());
    }

    @Test
    void test_hashCode() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), PRODUCT);
        assertEquals(Objects.hash(CLIENT_ID), wishlist.hashCode());
    }
}