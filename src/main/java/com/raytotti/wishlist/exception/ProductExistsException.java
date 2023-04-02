package com.raytotti.wishlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Product.exists")
public class ProductExistsException extends RuntimeException {

}
