package com.raytotti.wishlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PAYLOAD_TOO_LARGE, reason = "Wishlist.products.max.20")
public class MaxLimitProductException extends RuntimeException {

}
