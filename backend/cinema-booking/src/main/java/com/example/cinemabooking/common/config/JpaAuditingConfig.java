package com.example.cinemabooking.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Bat JPA Auditing de @CreatedDate / @LastModifiedDate trong BaseEntity tu dong duoc gan gia tri.
 * Neu thieu class nay, created_at va updated_at se luon null.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
