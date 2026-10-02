package com.example.cinemabooking.modules.schedule.entity;


import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.common.enums.DayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

/** Admin cau hinh khung "gio vang" theo loai ngay - dau vao cho thuat toan xep lich (GD6). */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "golden_hour_configs")
public class GoldenHourConfig extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "day_type", nullable = false, length = 20)
    private DayType dayType;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    /** He so uu tien cong vao priority_score cua showtime. */
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal weight = BigDecimal.ONE;
}

