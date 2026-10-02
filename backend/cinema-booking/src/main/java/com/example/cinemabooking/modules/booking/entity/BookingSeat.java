package com.example.cinemabooking.modules.booking.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.modules.showtime.entity.ShowtimeSeat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "booking_seats")
public class BookingSeat extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    /** unique: 1 ghe/suat chieu chi thuoc 1 booking tai 1 thoi diem. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_seat_id", nullable = false, unique = true)
    private ShowtimeSeat showtimeSeat;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
