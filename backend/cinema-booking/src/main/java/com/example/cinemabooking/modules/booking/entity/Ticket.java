package com.example.cinemabooking.modules.booking.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_seat_id", nullable = false, unique = true)
    private BookingSeat bookingSeat;

    @Column(name = "ticket_code", nullable = false, unique = true, length = 50)
    private String ticketCode;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt = LocalDateTime.now();

    @Column(name = "is_checked_in", nullable = false)
    private Boolean isCheckedIn = false;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;
}
