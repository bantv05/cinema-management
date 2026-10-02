package com.example.cinemabooking.modules.notification.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.common.enums.NotificationStatus;
import com.example.cinemabooking.common.enums.NotificationType;
import com.example.cinemabooking.modules.auth.entity.User;
import com.example.cinemabooking.modules.booking.entity.Booking;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    private String subject;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status = NotificationStatus.SENT;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();
}
