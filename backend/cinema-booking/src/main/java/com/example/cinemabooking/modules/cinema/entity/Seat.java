package com.example.cinemabooking.modules.cinema.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.common.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ghe VAT LY co dinh cua mot phong (khac voi ShowtimeSeat - trang thai ghe
 * theo TUNG suat chieu). Day la ket qua cua buoc admin "ve" so do ghe dong.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "seats", uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "seat_row", "seat_column"}))
public class Seat extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "seat_row", nullable = false, length = 5)
    private String seatRow;

    @Column(name = "seat_column", nullable = false)
    private Integer seatColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatType seatType;

    /** Danh dau o nay la loi di, khong phai ghe ngoi. */
    @Column(name = "is_aisle", nullable = false)
    private Boolean isAisle = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActiveStatus status = ActiveStatus.ACTIVE;
}
