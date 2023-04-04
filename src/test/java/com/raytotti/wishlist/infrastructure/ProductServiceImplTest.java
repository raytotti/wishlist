package com.raytotti.wishlist.infrastructure;

import com.raytotti.wishlist.domain.SimpleProduct;
import com.raytotti.wishlist.exception.ProductNotFoundException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductServiceImplTest {

    private final String URL = "UrlSupportService";
    private final String GET_URI = "/api/v1/products";
    private final String PRODUCT_ID = ObjectId.get().toHexString();
    @Mock
    private RestTemplate restTemplate;
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        productService = new ProductServiceImpl(restTemplate, URL, GET_URI);
    }

    @Test
    void getProductById() {
        SimpleProduct simpleProduct = SimpleProduct.of(PRODUCT_ID,
                "PRODUCT-CODE",
                "Product Description",
                "Image URL",
                BigDecimal.TEN
        );
        String url = URL + GET_URI + "/" + PRODUCT_ID;
        ResponseEntity<SimpleProduct> ok = ResponseEntity.ok(simpleProduct);
        doReturn(ok).when(restTemplate).getForEntity(url, SimpleProduct.class);

        SimpleProduct response = productService.getProductById(PRODUCT_ID);

        assertEquals(PRODUCT_ID, response.getId());
    }

    @Test
    void getProductById_not_found() {
        String url = URL + GET_URI + "/" + PRODUCT_ID;
        ResponseEntity<Boolean> notFound = ResponseEntity.notFound().build();
        doReturn(notFound).when(restTemplate).getForEntity(url, SimpleProduct.class);

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(PRODUCT_ID));
    }

    @Test
    void getProductById_not_found_exception() {
        String url = URL + GET_URI + "/" + PRODUCT_ID;
        doThrow(new ProductNotFoundException()).when(restTemplate).getForEntity(url, SimpleProduct.class);

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(PRODUCT_ID));
    }
}