package com.spotme.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Activates {@code createdAt}/{@code updatedAt} auto-population for entities
 * extending {@link AuditableEntity}.
 * <p>
 * Kept separate from {@code SpotmeApplication} on purpose: web-layer slice
 * tests (e.g. {@code @WebMvcTest}) use the application class as their
 * configuration source, and having {@code @EnableJpaAuditing} directly on it
 * would force JPA infrastructure into contexts that never load any
 * {@code @Entity}, causing a "JPA metamodel must not be empty" failure.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

}
