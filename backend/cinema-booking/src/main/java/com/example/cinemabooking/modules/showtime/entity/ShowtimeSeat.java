package com.example.cinemabooking.modules.showtime.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.common.enums.ShowtimeSeatStatus;
import com.example.cinemabooking.modules.auth.entity.User;
import com.example.cinemabooking.modules.cinema.entity.Seat;
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
@Table(name = "showtime_seats", uniqueConstraints = @UniqueConstraint(columnNames = {"showtime_id", "seat_id"}))
public class ShowtimeSeat extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    /** = showtimes.base_price * seat_types.price_multiplier, tinh san luc sinh ghe. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShowtimeSeatStatus status = ShowtimeSeatStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locked_by_user_id")
    private User lockedByUser;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    /** Moc RabbitMQ dua vao (TTL) de tu dong tra ghe ve AVAILABLE neu qua han. */
    @Column(name = "lock_expire_at")
    private LocalDateTime lockExpireAt;
}
