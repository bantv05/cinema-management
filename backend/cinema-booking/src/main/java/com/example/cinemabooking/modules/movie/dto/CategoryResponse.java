package com.example.cinemabooking.modules.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Du lieu the loai tra ve cho client. */
@Getter
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
}
