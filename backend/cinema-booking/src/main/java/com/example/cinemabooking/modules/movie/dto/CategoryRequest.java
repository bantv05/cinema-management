package com.example.cinemabooking.modules.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Du lieu client gui len khi tao/sua the loai. */
@Getter
@Setter
@NoArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Tên thể loại không được để trống")
    @Size(max = 50, message = "Tên thể loại tối đa 50 ký tự")
    private String name;
}
