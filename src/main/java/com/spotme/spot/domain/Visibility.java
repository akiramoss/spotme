package com.spotme.spot.domain;

/**
 * Who can see a {@link Spot}.
 * <p>
 * Modeled from Beta 1 to avoid a future schema migration, but not yet
 * enforced anywhere — every spot behaves as {@code PRIVATE} in practice
 * until the social features (followers, sharing) are implemented.
 */
public enum Visibility {
    PRIVATE,
    FOLLOWERS,
    PUBLIC
}
