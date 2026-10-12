package com.example.cinemabooking.common.exception;

/**
 * Nem ra khi du lieu bi trung voi rang buoc unique (vd: ten the loai) -> HTTP 409.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
