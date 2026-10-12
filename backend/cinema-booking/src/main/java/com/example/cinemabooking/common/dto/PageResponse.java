package com.example.cinemabooking.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Ket qua phan trang gon nhe tra ve cho Frontend.
 * Khong tra thang Page<T> cua Spring vi JSON cua PageImpl dai, kho doc va khong on dinh giua cac phien ban.
 */
@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
