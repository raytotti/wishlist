package com.raytotti.wishlist.infrastructure;

import com.raytotti.wishlist.domain.SimpleProduct;
import com.raytotti.wishlist.exception.ProductNotFoundException;
import com.raytotti.wishlist.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final RestTemplate restTemplate;

    private final String URL;

    private final String GET_URI;

    public ProductServiceImpl(RestTemplate restTemplate,
                              @Value("${wishlist-support.url}") String URL,
                              @Value("${wishlist-support.get-product}") String GET_URI) {
        this.restTemplate = restTemplate;
        this.URL = URL;
        this.GET_URI = GET_URI;
    }

    @Override
    public SimpleProduct getProductById(final String productId) {
        log.info("ProductServiceImpl -> getProductById: Solicitado get do produto com id {}", productId);

        String url = URL + GET_URI + "/" + productId;
        log.info("ProductServiceImpl -> getProductById: URL {}", url);

        ResponseEntity<SimpleProduct> response;
        try {
            response = restTemplate.getForEntity(url, SimpleProduct.class);
            log.info("ProductServiceImpl -> getProductById: response: {}", response);
        } catch (Exception e) {
            log.info("ProductServiceImpl -> getProductById: error: {}", e.getMessage());
            throw new ProductNotFoundException();
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new ProductNotFoundException();
        }
    }
}
