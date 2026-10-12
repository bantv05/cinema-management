package com.example.cinemabooking.modules.movie.service.impl;

import com.example.cinemabooking.common.dto.PageResponse;
import com.example.cinemabooking.common.enums.MovieStatus;
import com.example.cinemabooking.common.exception.BadRequestException;
import com.example.cinemabooking.common.exception.ResourceNotFoundException;
import com.example.cinemabooking.modules.movie.dto.CategoryResponse;
import com.example.cinemabooking.modules.movie.dto.MovieRequest;
import com.example.cinemabooking.modules.movie.dto.MovieResponse;
import com.example.cinemabooking.modules.movie.entity.Category;
import com.example.cinemabooking.modules.movie.entity.Movie;
import com.example.cinemabooking.modules.movie.entity.MovieCategory;
import com.example.cinemabooking.modules.movie.repository.ICategoryRepository;
import com.example.cinemabooking.modules.movie.repository.IMovieCategoryRepository;
import com.example.cinemabooking.modules.movie.repository.IMovieRepository;
import com.example.cinemabooking.modules.movie.service.IMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService implements IMovieService {

    private final IMovieRepository movieRepository;
    private final ICategoryRepository categoryRepository;
    private final IMovieCategoryRepository movieCategoryRepository;

    @Override
    public PageResponse<MovieResponse> getMovies(MovieStatus status, String keyword, Pageable pageable) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        Page<Movie> movies = movieRepository.search(status, normalizedKeyword, pageable);

        // Lay the loai cho ca trang trong 1 query, roi gom theo movieId
        List<Long> movieIds = movies.getContent().stream().map(Movie::getId).toList();
        Map<Long, List<CategoryResponse>> categoriesByMovieId = movieIds.isEmpty()
                ? Map.of()
                : movieCategoryRepository.findWithCategoryByMovieIds(movieIds).stream()
                .collect(Collectors.groupingBy(
                        mc -> mc.getMovie().getId(),
                        Collectors.mapping(mc -> toCategoryResponse(mc.getCategory()), Collectors.toList())));

        Page<MovieResponse> responses = movies.map(movie ->
                toResponse(movie, categoriesByMovieId.getOrDefault(movie.getId(), List.of())));
        return PageResponse.of(responses);
    }

    @Override
    public MovieResponse getMovieById(Long id) {
        Movie movie = findMovie(id);
        return toResponse(movie, loadCategories(id));
    }

    @Override
    @Transactional
    public MovieResponse createMovie(MovieRequest request) {
        validateDates(request);
        List<Category> categories = findCategories(request.getCategoryIds());

        Movie movie = new Movie();
        applyRequest(movie, request);
        movie.setStatus(request.getStatus() != null ? request.getStatus() : MovieStatus.COMING_SOON);
        movieRepository.save(movie);

        saveMovieCategories(movie, categories);
        return toResponse(movie, toCategoryResponses(categories));
    }

    @Override
    @Transactional
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = findMovie(id);
        validateDates(request);
        List<Category> categories = findCategories(request.getCategoryIds());

        applyRequest(movie, request);
        if (request.getStatus() != null) {
            movie.setStatus(request.getStatus());
        }

        // Gan lai toan bo the loai: xoa lien ket cu roi tao lien ket moi
        movieCategoryRepository.deleteByMovieId(id);
        saveMovieCategories(movie, categories);
        return toResponse(movie, toCategoryResponses(categories));
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = findMovie(id);
        movie.setStatus(MovieStatus.ENDED);
    }

    private Movie findMovie(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phim", id));
    }

    private void validateDates(MovieRequest request) {
        if (request.getEndDate() != null && request.getEndDate().isBefore(request.getReleaseDate())) {
            throw new BadRequestException("Ngày ngừng chiếu phải sau hoặc bằng ngày khởi chiếu");
        }
    }

    /** Kiem tra moi categoryId deu ton tai; bo qua id bi gui trung lap. */
    private List<Category> findCategories(List<Long> categoryIds) {
        Set<Long> uniqueIds = new HashSet<>(categoryIds);
        List<Category> categories = categoryRepository.findAllById(uniqueIds);
        if (categories.size() != uniqueIds.size()) {
            Set<Long> foundIds = categories.stream().map(Category::getId).collect(Collectors.toSet());
            uniqueIds.removeAll(foundIds);
            throw new BadRequestException("Thể loại không tồn tại: id = " + uniqueIds);
        }
        return categories;
    }

    private void saveMovieCategories(Movie movie, List<Category> categories) {
        List<MovieCategory> links = categories.stream().map(category -> {
            MovieCategory link = new MovieCategory();
            link.setMovie(movie);
            link.setCategory(category);
            return link;
        }).toList();
        movieCategoryRepository.saveAll(links);
    }

    private List<CategoryResponse> loadCategories(Long movieId) {
        return movieCategoryRepository.findWithCategoryByMovieIds(List.of(movieId)).stream()
                .map(mc -> toCategoryResponse(mc.getCategory()))
                .toList();
    }

    private void applyRequest(Movie movie, MovieRequest request) {
        movie.setTitle(request.getTitle().trim());
        movie.setDescription(request.getDescription());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setEndDate(request.getEndDate());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setDirector(request.getDirector());
        movie.setCastList(request.getCastList());
        movie.setLanguage(request.getLanguage());
        movie.setSubtitle(request.getSubtitle());
        movie.setAgeRating(request.getAgeRating());
    }

    private List<CategoryResponse> toCategoryResponses(List<Category> categories) {
        return categories.stream()
                .sorted(Comparator.comparing(Category::getName))
                .map(this::toCategoryResponse)
                .toList();
    }

    private CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

    private MovieResponse toResponse(Movie movie, List<CategoryResponse> categories) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationMinutes(movie.getDurationMinutes())
                .releaseDate(movie.getReleaseDate())
                .endDate(movie.getEndDate())
                .posterUrl(movie.getPosterUrl())
                .trailerUrl(movie.getTrailerUrl())
                .director(movie.getDirector())
                .castList(movie.getCastList())
                .language(movie.getLanguage())
                .subtitle(movie.getSubtitle())
                .ageRating(movie.getAgeRating())
                .averageRating(movie.getAverageRating())
                .status(movie.getStatus())
                .categories(categories)
                .build();
    }
}
