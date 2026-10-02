package com.example.cinemabooking.modules.schedule.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.modules.cinema.entity.Room;
import com.example.cinemabooking.modules.movie.entity.Movie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "schedule_generation_logs")
public class ScheduleGenerationLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "proposed_start_time", nullable = false)
    private LocalDateTime proposedStartTime;

    @Column(name = "day_of_week_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal dayOfWeekScore = BigDecimal.ZERO;

    @Column(name = "time_slot_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal timeSlotScore = BigDecimal.ZERO;

    @Column(name = "golden_hour_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal goldenHourScore = BigDecimal.ZERO;

    @Column(name = "rating_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal ratingScore = BigDecimal.ZERO;

    @Column(name = "genre_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal genreScore = BigDecimal.ZERO;

    @Column(name = "total_score", nullable = false, precision = 6, scale = 2)
    private BigDecimal totalScore = BigDecimal.ZERO;

    /** true neu khung gio nay bi loai do phong da co suat chieu khac trung lich. */
    @Column(name = "is_conflict", nullable = false)
    private Boolean isConflict = false;

    /** true neu admin da duyet va he thong da tao showtime that tu de xuat nay. */
    @Column(name = "is_selected", nullable = false)
    private Boolean isSelected = false;
}