package com.example.cinemabooking.common.exception;

import com.example.cinemabooking.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bat tap trung moi exception tu Controller/Service va tra ve cung 1 dinh dang ApiResponse,
 * de Frontend chi can xu ly 1 kieu loi duy nhat.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Loi @Valid tren request body: tra ve map field -> thong bao loi. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        ApiResponse<Map<String, String>> body =
                new ApiResponse<>(false, "Dữ liệu không hợp lệ", errors, LocalDateTime.now());
        return ResponseEntity.badRequest().body(body);
    }

    /** JSON sai cu phap hoac sai kieu (vd: gui chu vao truong so, enum khong ton tai). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "Request body không đúng định dạng");
    }

    /** Sai kieu tham so tren URL (vd: /movies/abc, status=UNKNOWN). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, "Tham số '" + ex.getName() + "' không hợp lệ");
    }

    /** URL khong ton tai - neu khong bat rieng se roi vao handler Exception va bi tra 500. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResource(NoResourceFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Đường dẫn không tồn tại");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Phương thức " + ex.getMethod() + " không được hỗ trợ");
    }

    /** Vi pham rang buoc DB (unique, khoa ngoai) ma Service chua kiem tra truoc. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return build(HttpStatus.CONFLICT, "Dữ liệu đang được sử dụng hoặc bị trùng lặp");
    }

    /** Loi khong luong truoc: log day du o server, khong lo chi tiet ra client. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi hệ thống");
    }

    private ResponseEntity<ApiResponse<Void>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApiResponse.error(message));
    }
}
