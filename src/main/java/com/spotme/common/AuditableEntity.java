package com.spotme.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base class for entities that need creation/update timestamps.
 * <p>
 * Timestamps are populated automatically by Spring Data JPA auditing
 * (see {@link com.spotme.SpotmeApplication}, annotated with
 * {@code @EnableJpaAuditing}) and are intentionally exposed only via
 * getters — no service or controller should set these values manually.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
