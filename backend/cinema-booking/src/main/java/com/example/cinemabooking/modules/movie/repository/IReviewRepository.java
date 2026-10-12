package com.example.cinemabooking.modules.movie.repository;

import com.example.cinemabooking.modules.movie.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {
}
