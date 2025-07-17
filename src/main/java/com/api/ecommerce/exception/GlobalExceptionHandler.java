package com.api.ecommerce.exception;

import com.api.ecommerce.global.dto.ApiErrorResponseDTO;
import com.api.ecommerce.modules.order.exceptions.InsufficientStockException;
import com.api.ecommerce.modules.order.exceptions.OrderAlreadyPaidException;
import com.api.ecommerce.modules.order.exceptions.OrderNotFoundException;
import com.api.ecommerce.modules.product.exception.ProductNotFoundException;
import com.api.ecommerce.modules.user.exceptions.EmailAlreadyRegisteredException;
import com.api.ecommerce.modules.user.exceptions.UserNotAuthenticatedException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InsufficientStockException.class,
            EmailAlreadyRegisteredException.class
    })
    public ResponseEntity<ApiErrorResponseDTO> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            OrderAlreadyPaidException.class
    })
    public ResponseEntity<ApiErrorResponseDTO> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleUnauthorized(UserNotAuthenticatedException ex, HttpServletRequest request) {
        return buildResponse(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse("Access denied.", request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            OrderNotFoundException.class,
            ProductNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponseDTO> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder message = new StringBuilder("Validation failed: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> message.append(error.getField())
                .append(" - ")
                .append(error.getDefaultMessage())
                .append("; "));

        return buildResponse(message.toString(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO> handleGeneric(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildResponse("Internal Server Error.", request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiErrorResponseDTO> buildResponse(Exception ex, HttpServletRequest request, HttpStatus status) {
        return buildResponse(ex.getMessage(), request, status);
    }

    private ResponseEntity<ApiErrorResponseDTO> buildResponse(String message, HttpServletRequest request, HttpStatus status) {
        ApiErrorResponseDTO error = new ApiErrorResponseDTO(
                LocalDateTime.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }
}
