package com.example.cinemabooking.modules.movie.service;

import com.example.cinemabooking.common.dto.PageResponse;
import com.example.cinemabooking.common.enums.MovieStatus;
import com.example.cinemabooking.modules.movie.dto.MovieRequest;
import com.example.cinemabooking.modules.movie.dto.MovieResponse;
import org.springframework.data.domain.Pageable;

public interface IMovieService {

    PageResponse<MovieResponse> getMovies(MovieStatus status, String keyword, Pageable pageable);

    MovieResponse getMovieById(Long id);

    MovieResponse createMovie(MovieRequest request);

    MovieResponse updateMovie(Long id, MovieRequest request);

    /** Xoa mem: chuyen status sang ENDED, giu lai du lieu cho suat chieu/booking cu. */
    void deleteMovie(Long id);
}
