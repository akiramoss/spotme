package com.spotme.spot.domain;

/**
 * Classification of a {@link Spot}, chosen by the user at creation time.
 * <p>
 * Persisted as {@code STRING} (see {@link Spot#category}), not {@code ORDINAL} —
 * new values can be inserted anywhere in this list without corrupting
 * existing data.
 */
public enum Category {
    CAFE,
    RESTAURANT,
    BAR,
    PARK,
    MUSEUM,
    STUDY_SPOT,
    VIEWPOINT,
    SHOP,
    BEACH,
    GYM,
    OTHER
}
