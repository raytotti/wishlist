package com.raytotti.wishlist.service;

import com.raytotti.wishlist.domain.SimpleProduct;

public interface ProductService {
    SimpleProduct getProductById(String productId);
}
