package com.example.cinemabooking.modules.booking.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.common.enums.BookingStatus;
import com.example.cinemabooking.modules.auth.entity.User;
import com.example.cinemabooking.modules.showtime.entity.Showtime;
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
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @Column(name = "booking_code", nullable = false, unique = true, length = 20)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.PENDING;

    /** Moc RabbitMQ dua vao de tu dong huy neu qua han thanh toan. */
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;
}
