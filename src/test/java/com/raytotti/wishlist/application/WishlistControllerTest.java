package com.raytotti.wishlist.application;

import com.raytotti.wishlist.domain.SimpleProduct;
import com.raytotti.wishlist.domain.Wishlist;
import com.raytotti.wishlist.domain.WishlistRepository;
import com.raytotti.wishlist.exception.ClientNotFoundException;
import com.raytotti.wishlist.exception.WishlistNotFoundException;
import com.raytotti.wishlist.service.ClientService;
import com.raytotti.wishlist.service.ProductService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

class WishlistControllerTest {

    private final ObjectId CLIENT_ID = ObjectId.get();
    private final String PRODUCT_ID = ObjectId.get().toHexString();
    private final String CODE = "PRODUCT-CODE";
    private final String DESCRIPTION = "Product Description";
    private final String THUMBNAIL = "Image URL";
    private final BigDecimal PRICE = BigDecimal.TEN;
    private final WishlistAddProductRequest REQUEST = new WishlistAddProductRequest(PRODUCT_ID);
    private final SimpleProduct SIMPLE_PRODUCT = SimpleProduct.of(
            PRODUCT_ID,
            CODE,
            DESCRIPTION,
            THUMBNAIL,
            PRICE
    );

    @Mock
    private Wishlist wishlistMock = Wishlist.of(CLIENT_ID.toHexString(), SIMPLE_PRODUCT);
    @Mock
    private WishlistRepository repository;
    @Mock
    private ClientService clientService;
    @Mock
    private ProductService productService;
    private WishlistController wishlistController;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        wishlistController = new WishlistController(repository, clientService, productService);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setContextPath("/api/v1/wishlists");
        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attrs);
    }

    @Test
    public void addProduct_first_product() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), SIMPLE_PRODUCT);

        doReturn(Optional.empty()).when(repository).findByClientId(CLIENT_ID);
        doReturn(wishlist).when(repository).save(wishlist);
        doReturn(true).when(clientService).existsClientId(CLIENT_ID.toHexString());
        doReturn(SIMPLE_PRODUCT).when(productService).getProductById(PRODUCT_ID);

        ResponseEntity<WishlistResponse> response = wishlistController.addProduct(CLIENT_ID.toHexString(), REQUEST);

        assertNotNull(response.getHeaders().get("location"));
        assertEquals(1, Objects.requireNonNull(response.getBody()).getProducts().size());
        assertEquals(CLIENT_ID.toHexString(), Objects.requireNonNull(response.getBody()).getClientId());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void addProduct_more_than_one() {
        Wishlist wishlistFind = Wishlist.of(CLIENT_ID.toHexString(), SIMPLE_PRODUCT);

        Wishlist wishlistSave = Wishlist.of(CLIENT_ID.toHexString(), SIMPLE_PRODUCT);
        String otherProductId = ObjectId.get().toHexString();
        SimpleProduct otherProduct = SimpleProduct.of(
                otherProductId,
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE
        );
        wishlistSave.addProduct(otherProduct);

        doReturn(Optional.of(wishlistFind)).when(repository).findByClientId(CLIENT_ID);
        doReturn(wishlistSave).when(repository).save(wishlistSave);
        doReturn(true).when(clientService).existsClientId(CLIENT_ID.toHexString());
        doReturn(otherProduct).when(productService).getProductById(otherProductId);

        ResponseEntity<WishlistResponse> response = wishlistController.addProduct(CLIENT_ID.toHexString(), new WishlistAddProductRequest(otherProductId));

        assertNotNull(response.getHeaders().get("location"));
        assertEquals(2, Objects.requireNonNull(response.getBody()).getProducts().size());
        assertEquals(CLIENT_ID.toHexString(), Objects.requireNonNull(response.getBody()).getClientId());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void addProduct_client_not_found() {
        doReturn(Optional.empty()).when(repository).findByClientId(CLIENT_ID);
        doReturn(false).when(clientService).existsClientId(CLIENT_ID.toHexString());

        assertThrows(ClientNotFoundException.class, () -> wishlistController.addProduct(CLIENT_ID.toHexString(), REQUEST));
    }

    @Test
    public void removeProduct_one_product() {
        SimpleProduct otherProduct = SimpleProduct.of(
                ObjectId.get().toHexString(),
                CODE,
                DESCRIPTION,
                THUMBNAIL,
                PRICE
        );
        Wishlist wishlistFind = Wishlist.of(CLIENT_ID.toHexString(), SIMPLE_PRODUCT);
        wishlistFind.addProduct(otherProduct);
        doReturn(Optional.of(wishlistFind)).when(repository).findByClientId(CLIENT_ID);

        Wishlist wishlistSave = Wishlist.of(CLIENT_ID.toHexString(), SIMPLE_PRODUCT);
        wishlistSave.addProduct(otherProduct);
        wishlistSave.removeProduct(PRODUCT_ID);
        doReturn(wishlistSave).when(repository).save(wishlistSave);

        ResponseEntity<Void> response = wishlistController.removeProduct(CLIENT_ID.toHexString(), PRODUCT_ID);

        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void removeProduct_product_and_wishlist() {
        String wishlistId = ObjectId.get().toHexString();
        doReturn(Optional.of(wishlistMock)).when(repository).findByClientId(CLIENT_ID);
        doReturn(wishlistId).when(wishlistMock).getId();

        doNothing().when(repository).deleteById(wishlistId);

        ResponseEntity<Void> response = wishlistController.removeProduct(CLIENT_ID.toHexString(), PRODUCT_ID);

        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void removeProduct_wishlist_not_found() {
        doReturn(Optional.empty()).when(repository).findByClientId(CLIENT_ID);

        assertThrows(WishlistNotFoundException.class, () -> wishlistController.removeProduct(CLIENT_ID.toHexString(), PRODUCT_ID));
    }

    @Test
    public void existsProduct_found() {
        doReturn(true).when(repository).existsByClientIdAndProductsId(CLIENT_ID, new ObjectId(PRODUCT_ID));

        ResponseEntity<Boolean> response = wishlistController.existsProduct(CLIENT_ID.toHexString(), PRODUCT_ID);

        assertNotNull(response);
        assertEquals(Boolean.TRUE, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void existsProduct_not_found() {
        ObjectId productId = ObjectId.get();
        doReturn(false).when(repository).existsByClientIdAndProductsId(CLIENT_ID, productId);

        ResponseEntity<Boolean> response = wishlistController.existsProduct(CLIENT_ID.toHexString(), productId.toHexString());

        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByClientId_found() {
        Wishlist wishlist = Wishlist.of(CLIENT_ID.toHexString(), SIMPLE_PRODUCT);

        doReturn(Optional.of(wishlist)).when(repository).findByClientId(CLIENT_ID);

        ResponseEntity<WishlistResponse> response = wishlistController.findByClientId(CLIENT_ID.toHexString());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findByClientId_not_found() {
        doReturn(Optional.empty()).when(repository).findByClientId(CLIENT_ID);

        assertThrows(WishlistNotFoundException.class, () -> wishlistController.findByClientId(CLIENT_ID.toHexString()));
    }
}
