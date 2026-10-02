package com.example.cinemabooking.modules.movie.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.modules.auth.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id", "user_id"}))
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;

    @Lob
    private String comment;
}