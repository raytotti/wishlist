package com.raytotti.wishlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Wishlist.notFound")
public class WishlistNotFoundException extends RuntimeException {

}
