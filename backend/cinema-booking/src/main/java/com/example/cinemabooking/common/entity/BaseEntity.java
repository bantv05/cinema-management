package com.example.cinemabooking.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity cha cho toan bo he thong - moi bang trong ERD (users, movies, bookings...)
 * deu nen extends class nay de co san id + created_at + updated_at, tranh lap code.
 *
 * Vi du dung:
 *   @Entity
 *   @Table(name = "movies")
 *   public class Movie extends BaseEntity { ... cac field rieng cua movies ... }
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonIgnore
    private LocalDateTime updatedAt;
}
