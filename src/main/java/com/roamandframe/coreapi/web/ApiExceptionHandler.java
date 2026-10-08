package com.roamandframe.coreapi.web;

import com.roamandframe.coreapi.modules.cart.exception.InsufficientStockException;
import com.roamandframe.coreapi.modules.cart.exception.EmptyCartException;
import com.roamandframe.coreapi.modules.cart.exception.InvalidCartException;
import com.roamandframe.coreapi.modules.catalog.exception.ProductNotFoundException;
import com.roamandframe.coreapi.modules.customer.exception.CustomerNotFoundException;
import com.roamandframe.coreapi.modules.inventory.exception.StockReservationException;
import com.roamandframe.coreapi.modules.order.exception.OrderNotFoundException;
import com.roamandframe.coreapi.modules.payment.exception.PaymentDeclinedException;
import com.roamandframe.coreapi.modules.shipping.exception.DeliveryAddressIncompleteException;
import com.roamandframe.coreapi.security.exception.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    ProblemDetail handleInvalidCredentials(InvalidCredentialsException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problem.setTitle("Authentication failed");
        return problem;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    ProblemDetail handleCustomerNotFound(CustomerNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Customer not found");
        return problem;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    ProblemDetail handleProductNotFound(ProductNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Product not found");
        return problem;
    }

    @ExceptionHandler(InsufficientStockException.class)
    ProblemDetail handleInsufficientStock(InsufficientStockException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Insufficient stock");
        return problem;
    }

    @ExceptionHandler({EmptyCartException.class, InvalidCartException.class,
            DeliveryAddressIncompleteException.class, StockReservationException.class})
    ProblemDetail handleCheckoutConflict(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Checkout not possible");
        return problem;
    }

    @ExceptionHandler(PaymentDeclinedException.class)
    ProblemDetail handlePaymentDeclined(PaymentDeclinedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.PAYMENT_REQUIRED, ex.getMessage());
        problem.setTitle("Payment declined");
        return problem;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    ProblemDetail handleOrderNotFound(OrderNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Order not found");
        return problem;
    }
}
