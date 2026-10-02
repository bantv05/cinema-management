package com.example.cinemabooking.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Bao boc chuan cho MOI response tra ve tu API, giup Frontend (ReactJS)
 * luon parse theo 1 cau truc duy nhat du la thanh cong hay loi.
 *
 * Vi du JSON tra ve:
 * { "success": true, "message": "OK", "data": {...}, "timestamp": "..." }
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Thanh cong", data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now());
    }
}
