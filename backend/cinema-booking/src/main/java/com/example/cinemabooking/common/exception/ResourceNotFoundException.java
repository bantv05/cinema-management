package com.example.cinemabooking.common.exception;

/**
 * Nem ra khi khong tim thay ban ghi theo id/khoa -> GlobalExceptionHandler tra ve HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + " không tồn tại với id = " + id);
    }
}
