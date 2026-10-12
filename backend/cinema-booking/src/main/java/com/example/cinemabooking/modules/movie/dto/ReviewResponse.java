package com.example.cinemabooking.modules.movie.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Du lieu review tra ve cho client.
 * Chi lo userId + fullName + avatarUrl, KHONG tra ca entity User (tranh lo email, password hash...).
 */
@Getter
@Builder
public class ReviewResponse {

    private Long id;
    private Long movieId;
    private Long userId;
    private String userFullName;
    private String userAvatarUrl;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
