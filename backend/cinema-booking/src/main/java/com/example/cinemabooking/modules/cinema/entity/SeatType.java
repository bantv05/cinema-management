package com.example.cinemabooking.modules.cinema.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "seat_types")
public class SeatType extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "price_multiplier", nullable = false, precision = 4, scale = 2)
    private BigDecimal priceMultiplier = BigDecimal.ONE;

    @Column(name = "color_code", length = 10)
    private String colorCode;
}

