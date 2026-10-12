package com.example.cinemabooking.common.exception;

/**
 * Nem ra khi du lieu hop le ve dinh dang nhung sai nghiep vu (vd: end_date truoc release_date) -> HTTP 400.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
