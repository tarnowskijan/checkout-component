package org.shopping.endpoint;

import org.shopping.domain.cart.ProductNotFoundException;
import org.shopping.domain.cart.ShoppingCartNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ExceptionHandlerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({ShoppingCartNotFoundException.class, ProductNotFoundException.class})
    ResponseEntity<?> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Throwable.class)
    ResponseEntity<?> handleUnexpectedException(Throwable e) {
        logger.warn("Unexpected exception occurred.", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
