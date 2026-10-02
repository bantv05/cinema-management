package com.example.cinemabooking.modules.auth.entity;

import com.example.cinemabooking.common.entity.BaseEntity;
import com.example.cinemabooking.common.enums.UserProvider;
import com.example.cinemabooking.common.enums.UserRole;
import com.example.cinemabooking.common.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tai khoan nguoi dung - ho tro dang nhap thuong (LOCAL) va dang nhap Google (GOOGLE).
 * Khi provider = GOOGLE thi password co the de null.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank
    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @NotBlank
    @Email
    @Column(length = 150, nullable = false, unique = true)
    private String email;

    /** Null neu dang nhap bang Google. */
    @Column(length = 255)
    private String password;

    @Column(length = 15)
    private String phone;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "google_id", unique = true)
    private String googleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserProvider provider = UserProvider.LOCAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.CUSTOMER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;
}
