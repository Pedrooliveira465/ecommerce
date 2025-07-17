package com.api.ecommerce.global.dto;

public record ApiErrorResponseDTO(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {}
