package com.example.cinemabooking.modules.movie.repository;

import com.example.cinemabooking.common.enums.MovieStatus;
import com.example.cinemabooking.modules.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Loc phim theo trang thai (null = tat ca) va tu khoa trong ten phim.
     * keyword khong duoc null (Service truyen "" khi khong loc).
     */
    @Query("""
            SELECT m FROM Movie m
            WHERE (:status IS NULL OR m.status = :status)
              AND LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Movie> search(@Param("status") MovieStatus status,
                       @Param("keyword") String keyword,
                       Pageable pageable);
}
