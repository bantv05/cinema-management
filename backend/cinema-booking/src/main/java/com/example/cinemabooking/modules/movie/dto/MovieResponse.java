package com.example.cinemabooking.modules.movie.dto;

import com.example.cinemabooking.common.enums.MovieStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Du lieu phim tra ve cho client, kem danh sach the loai. */
@Getter
@Builder
public class MovieResponse {

    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private String posterUrl;
    private String trailerUrl;
    private String director;
    private String castList;
    private String language;
    private String subtitle;
    private String ageRating;
    private BigDecimal averageRating;
    private MovieStatus status;
    private List<CategoryResponse> categories;
}
