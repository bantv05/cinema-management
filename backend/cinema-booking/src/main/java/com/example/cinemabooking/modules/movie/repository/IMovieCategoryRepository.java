package com.example.cinemabooking.modules.movie.repository;

import com.example.cinemabooking.modules.movie.entity.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IMovieCategoryRepository extends JpaRepository<MovieCategory, Long> {


    @Query("""
            SELECT mc FROM MovieCategory mc
            JOIN FETCH mc.category c
            WHERE mc.movie.id IN :movieIds
            ORDER BY c.name
            """)
    List<MovieCategory> findWithCategoryByMovieIds(@Param("movieIds") Collection<Long> movieIds);

    @Modifying
    @Query("DELETE FROM MovieCategory mc WHERE mc.movie.id = :movieId")
    void deleteByMovieId(@Param("movieId") Long movieId);

    boolean existsByCategoryId(Long categoryId);
}
