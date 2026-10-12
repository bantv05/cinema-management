package com.example.cinemabooking.modules.movie.controller;

import com.example.cinemabooking.common.dto.ApiResponse;
import com.example.cinemabooking.common.dto.PageResponse;
import com.example.cinemabooking.common.enums.MovieStatus;
import com.example.cinemabooking.common.exception.BadRequestException;
import com.example.cinemabooking.modules.movie.dto.MovieRequest;
import com.example.cinemabooking.modules.movie.dto.MovieResponse;
import com.example.cinemabooking.modules.movie.service.IMovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.prefix}/movies")
@RequiredArgsConstructor
public class MovieController {

    private static final int MAX_PAGE_SIZE = 10;

    private final IMovieService movieService;

    /** Sap xep co dinh theo phim moi nhat, khong nhan sort tu client de tranh loi field khong ton tai. */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MovieResponse>>> getMovies(
            @RequestParam(required = false) MovieStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size < 1 || size > MAX_PAGE_SIZE) {
            throw new BadRequestException("page phải >= 0 và size trong khoảng 1-" + MAX_PAGE_SIZE);
        }
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(ApiResponse.success(movieService.getMovies(status, keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(movieService.getMovieById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MovieResponse>> createMovie(@Valid @RequestBody MovieRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo phim thành công", movieService.createMovie(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> updateMovie(@PathVariable Long id,
                                                                  @Valid @RequestBody MovieRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật phim thành công", movieService.updateMovie(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok(ApiResponse.success("Đã ngừng chiếu phim", null));
    }
}
