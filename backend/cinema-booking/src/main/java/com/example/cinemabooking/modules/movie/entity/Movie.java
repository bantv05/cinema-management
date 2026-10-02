package com.example.cinemabooking.modules.movie.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.common.enums.MovieStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie extends BaseEntity {
    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    private String description;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    /** Ngay ngung chieu - dung de loc phim con hop le khi chay thuat toan xep lich (GD6). */
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "trailer_url")
    private String trailerUrl;

    private String director;

    @Lob
    @Column(name = "cast_list")
    private String castList;

    private String language;

    private String subtitle;

    /** P, C13, C16, C18... */
    @Column(name = "age_rating", length = 10)
    private String ageRating;

    /** Cache diem trung binh tu bang reviews - input cho thuat toan xep lich (GD6). */
    @Column(name = "average_rating", precision = 3, scale = 1, nullable = false)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MovieStatus status = MovieStatus.COMING_SOON;

}
